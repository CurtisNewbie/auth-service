package com.curtisnewbie.service.auth.web.open.api.boundary;

import com.curtisnewbie.common.trace.TUser;
import com.curtisnewbie.common.trace.TraceUtils;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.common.vo.*;
import com.curtisnewbie.goauth.client.*;
import com.curtisnewbie.service.auth.dao.*;
import com.curtisnewbie.service.auth.local.api.*;
import com.curtisnewbie.service.auth.messaging.helper.LogOperation;
import com.curtisnewbie.service.auth.messaging.services.AuthMessageDispatcher;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import com.curtisnewbie.service.auth.remote.exception.InvalidAuthenticationException;
import com.curtisnewbie.service.auth.remote.vo.*;
import com.curtisnewbie.service.auth.web.open.api.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.curtisnewbie.common.util.AssertUtils.*;
import static com.curtisnewbie.service.auth.util.UserValidator.validatePassword;
import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * User Controller
 *
 * @author yongjie.zhuang
 */
@RestController
@RequestMapping("${web.base-path}/user")
public class UserController {
    public static final String LOGIN_URL = "/auth-service/open/api/user/login";

    @Autowired
    private UserService userService;
    @Autowired
    private AuthMessageDispatcher authMessageDispatcher;
    @Autowired
    private GoAuthClient goAuthClient;

    /**
     * Login (no role control)
     */
    @PathDoc(description = "Login", type = PathType.PUBLIC)
    @PostMapping("/login")
    public Result<String> login(@Validated @RequestBody LoginWebVo loginWebVo,
                                @RequestHeader(value = "x-forwarded-for", required = false) String forwardedFor,
                                @RequestHeader(value = "user-agent", required = false) String userAgent) {

        String remoteAddr = forwardedFor;
        if (remoteAddr == null) remoteAddr = "unknown";
        else {
            final String[] sff = forwardedFor.split(",");
            if (sff.length > 0) remoteAddr = sff[0];
        }

        // login and generate an access token
        final String token = userService.exchangeToken(loginWebVo.getUsername(), loginWebVo.getPassword());

        // log the access asynchronously
        final AccessLogInfoVo p = new AccessLogInfoVo();
        p.setUserAgent(StringUtils.substring(userAgent, 0, 512));
        p.setIpAddress(remoteAddr);
        p.setUserId(0);
        p.setUsername(loginWebVo.getUsername());
        p.setUrl(LOGIN_URL);
        authMessageDispatcher.dispatchAccessLog(p);
        return Result.of(token);
    }

    /**
     * Add User (Only Admin is permitted)
     */
    @PathDoc(description = "Admin add user", resourceCode = Resources.ADMIN_MNG_USER, resourceName = Resources.ADMIN_MNG_USER_NAME)
    @LogOperation(name = "addUser", description = "Add user")
    @PostMapping("/add")
    public Result<?> addUser(@Validated @RequestBody AddUserVo param) {

        // validate roleNo
        if (isNotBlank(param.getRoleNo())) {
            final RoleInfoReq rir = new RoleInfoReq();
            rir.setRoleNo(param.getRoleNo());
            final Result<RoleInfoResp> roleInfo = goAuthClient.getRoleInfo(rir);
            roleInfo.assertIsOk();
        }

        userService.addUser(param);
        return Result.ok();
    }

    /**
     * List users (only admin)
     */
    @PathDoc(description = "Admin list users", resourceCode = Resources.ADMIN_MNG_USER, resourceName = Resources.ADMIN_MNG_USER_NAME)
    @PostMapping("/list")
    public Result<PageableList<UserInfoVo>> listUsers(@RequestBody ListUserReq req) {
        return Result.of(userService.findUserInfoByPage(req));
    }

    /**
     * Delete user logically (only admin)
     */
    @PathDoc(description = "Admin delete user", resourceCode = Resources.ADMIN_MNG_USER, resourceName = Resources.ADMIN_MNG_USER_NAME)
    @LogOperation(name = "deleteUser", description = "Delete user")
    @PostMapping("/delete")
    public Result<Void> deleteUser(@RequestBody DeleteUserReqWebVo reqVo) throws InvalidAuthenticationException {
        TUser tUser = TraceUtils.tUser();
        userService.deleteUserLogically(reqVo.getId(), tUser.getUsername());
        return Result.ok();
    }

    /**
     * Update user info (only admin)
     */
    @PathDoc(description = "Admin update user info", resourceCode = Resources.ADMIN_MNG_USER, resourceName = Resources.ADMIN_MNG_USER_NAME)
    @LogOperation(name = "updateUserInfo", description = "Update user info")
    @PostMapping("/info/update")
    public Result<Void> updateUserInfo(@Validated @RequestBody UpdateUserInfoReqVo param) {
        TUser tUser = TraceUtils.tUser();
        notEquals(param.getId(), TraceUtils.tUser().getUserId(), "You cannot update yourself");

        UserIsDisabled isDisabled = null;
        if (param.getIsDisabled() != null) isDisabled = EnumUtils.parse(param.getIsDisabled(), UserIsDisabled.class);
        if (param.getRoleNo() != null) param.setRoleNo(param.getRoleNo().trim());

        // validate roleNo
        if (isNotBlank(param.getRoleNo())) {
            final RoleInfoReq rir = new RoleInfoReq();
            rir.setRoleNo(param.getRoleNo());
            final Result<RoleInfoResp> roleInfo = goAuthClient.getRoleInfo(rir);
            roleInfo.assertIsOk();
        }

        userService.updateUser(UpdateUserVo.builder()
                .id(param.getId())
                .isDisabled(isDisabled)
                .updateBy(tUser.getUsername())
                .roleNo(param.getRoleNo())
                .build());
        return Result.ok();
    }

    @PathDoc(description = "Admin review user registration", resourceCode = Resources.ADMIN_MNG_USER, resourceName = Resources.ADMIN_MNG_USER_NAME)
    @LogOperation(name = "reviewRegistration", description = "User registration review")
    @PostMapping("/registration/review")
    public Result<Void> reviewRegistration(@Valid @RequestBody UserReviewCmd cmd) {
        userService.reviewUserRegistration(cmd);
        return Result.ok();
    }

    /*
    -------------------------------------------------------------

    Doesn't require admin

    -------------------------------------------------------------
     */

    /**
     * Registration request (no role control)
     */
    @PathDoc(description = "User request registration", type = PathType.PUBLIC)
    @PostMapping("/register/request")
    public Result<?> register(@RequestBody RegisterUserVo vo) {
        userService.register(vo);
        return Result.ok();
    }

    /**
     * Get user info (no role control)
     */
    @PathDoc(description = "User get user info", resourceCode = Resources.BASIC_USER, resourceName = Resources.BASIC_USER_NAME)
    @GetMapping("/info")
    public Result<UserWebVo> getUserInfo() {
        if (!TraceUtils.isLoggedIn())
            return Result.ok();

        final UserWebVo uv = userService.loadUserInfo(TraceUtils.tUser().getUsername());
        if (isNotBlank(uv.getRoleNo())) {
            uv.setRoleName(fetchRoleName(uv.getRoleNo()));
        }
        return Result.of(uv);
    }

    private String fetchRoleName(String roleNo) {
        final RoleInfoReq rir = new RoleInfoReq();
        rir.setRoleNo(roleNo);
        final RoleInfoResp rr = Result.tryGetData(goAuthClient.getRoleInfo(rir), () -> String.format("goAuthClient.getRoleInfo, roleNo: %s", roleNo));
        if (rr != null) {
            return rr.getName();
        }
        return null;
    }

    /**
     * Get user info (no role control)
     */
    @PathDoc(description = "User get user details", resourceCode = Resources.BASIC_USER, resourceName = Resources.BASIC_USER_NAME)
    @GetMapping("/detail")
    public Result<UserDetailVo> getUserDetail() {
        final String username = TraceUtils.tUser().getUsername();
        User u = userService.loadUserByUsername(username);
        UserDetailVo ud = BeanCopyUtils.toType(u, UserDetailVo.class);
        if (u.getCreateTime() != null)
            ud.setRegisterDate(u.getCreateTime().toLocalDate().toString());
        if (isNotBlank(ud.getRoleNo()))
            ud.setRoleName(fetchRoleName(ud.getRoleNo()));
        return Result.of(ud);
    }

    /**
     * Update password (no role control)
     */
    @PathDoc(description = "User update password", resourceCode = Resources.BASIC_USER, resourceName = Resources.BASIC_USER_NAME)
    @PostMapping("/password/update")
    public Result<Void> updatePassword(@RequestBody UpdatePasswordWebVo vo) {
        final TUser tUser = TraceUtils.tUser();

        final String newPassword = vo.getNewPassword();
        final String prevPassword = vo.getPrevPassword();
        hasText(newPassword, "New password is required");
        hasText(prevPassword, "Old password is required");

        // check if the old password and prev password are equal
        notEquals(newPassword, prevPassword, "New password must be different");

        // validate if the new password is too short
        validatePassword(newPassword);

        userService.updatePassword(newPassword, prevPassword, tUser.getUserId());
        return Result.ok();
    }

}
