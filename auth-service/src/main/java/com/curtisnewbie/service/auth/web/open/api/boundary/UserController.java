package com.curtisnewbie.service.auth.web.open.api.boundary;

import brave.Tracer;
import com.curtisnewbie.common.advice.RoleControlled;
import com.curtisnewbie.common.trace.TUser;
import com.curtisnewbie.common.trace.TraceUtils;
import com.curtisnewbie.common.util.AsyncUtils;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.common.vo.*;
import com.curtisnewbie.goauth.client.GoAuthClient;
import com.curtisnewbie.goauth.client.RoleInfoReq;
import com.curtisnewbie.goauth.client.RoleInfoResp;
import com.curtisnewbie.service.auth.dao.*;
import com.curtisnewbie.service.auth.local.api.*;
import com.curtisnewbie.service.auth.messaging.helper.LogOperation;
import com.curtisnewbie.service.auth.messaging.services.AuthMessageDispatcher;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.exception.InvalidAuthenticationException;
import com.curtisnewbie.service.auth.remote.vo.*;
import com.curtisnewbie.service.auth.web.open.api.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;

import java.util.Optional;

import static com.curtisnewbie.common.util.AssertUtils.*;
import static com.curtisnewbie.common.util.BeanCopyUtils.*;
import static com.curtisnewbie.service.auth.util.UserValidator.validatePassword;
import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * User Controller
 *
 * @author yongjie.zhuang
 */
@Slf4j
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
        final String token = userService.exchangeToken(loginWebVo.getUsername(), loginWebVo.getPassword(), loginWebVo.getAppName());

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
    @LogOperation(name = "addUser", description = "Add user")
    @RoleControlled(rolesRequired = "admin")
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
    @RoleControlled(rolesRequired = "admin")
    @PostMapping("/list")
    public Result<GetUserListRespWebVo> getUserList(@RequestBody GetUserListReqWebVo reqVo) {
        FindUserInfoVo searchParam = toFindUserInfoVo(reqVo);
        PageableList<UserInfoVo> pps = userService.findUserInfoByPage(searchParam);
        GetUserListRespWebVo resp = new GetUserListRespWebVo();
        resp.setList(toTypeList(pps.getPayload(), UserInfoWebVo.class));
        resp.setPagingVo(pps.getPagingVo());
        return Result.of(resp);
    }

    /**
     * Delete user logically (only admin)
     */
    @LogOperation(name = "deleteUser", description = "Delete user")
    @RoleControlled(rolesRequired = "admin")
    @PostMapping("/delete")
    public Result<Void> deleteUser(@RequestBody DeleteUserReqWebVo reqVo) throws InvalidAuthenticationException {
        TUser tUser = TraceUtils.tUser();
        Assert.isTrue(UserRole.isAdmin(tUser.getRole()), "Not permitted");

        userService.deleteUserLogically(reqVo.getId(), tUser.getUsername());
        return Result.ok();
    }

    /**
     * Update user info (only admin)
     */
    @LogOperation(name = "updateUserInfo", description = "Update user info")
    @RoleControlled(rolesRequired = "admin")
    @PostMapping("/info/update")
    public Result<Void> updateUserInfo(@RequestBody UpdateUserInfoReqVo param) {
        TUser tUser = TraceUtils.tUser();
        Assert.isTrue(UserRole.isAdmin(tUser.getRole()), "Not permitted");

        nonNull(param.getId(), "id == null");
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

    @LogOperation(name = "reviewRegistration", description = "User registration review")
    @RoleControlled(rolesRequired = "admin")
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
    @PostMapping("/register/request")
    public Result<?> register(@RequestBody RegisterUserVo vo) {
        userService.register(vo);
        return Result.ok();
    }

    /**
     * Get user info (no role control)
     */
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

    // --------------------------------- private helper methods ------------------------------------

    private static FindUserInfoVo toFindUserInfoVo(GetUserListReqWebVo reqVo) {
        FindUserInfoVo infoVo = new FindUserInfoVo();
        infoVo.setUsername(reqVo.getUsername());
        infoVo.setPagingVo(reqVo.getPagingVo());
        if (reqVo.getIsDisabled() != null) infoVo.setIsDisabled(reqVo.getIsDisabled());
        return infoVo;
    }

}
