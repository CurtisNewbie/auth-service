package com.curtisnewbie.service.auth.web.open.api.boundary;

import com.curtisnewbie.common.advice.RoleControlled;
import com.curtisnewbie.common.trace.TUser;
import com.curtisnewbie.common.trace.TraceUtils;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.common.vo.*;
import com.curtisnewbie.gateway.utils.HttpHeadersUtils;
import com.curtisnewbie.service.auth.dao.*;
import com.curtisnewbie.service.auth.infrastructure.converters.UserWebConverter;
import com.curtisnewbie.service.auth.local.api.*;
import com.curtisnewbie.service.auth.messaging.helper.LogOperation;
import com.curtisnewbie.service.auth.messaging.services.AuthMessageDispatcher;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.exception.InvalidAuthenticationException;
import com.curtisnewbie.service.auth.remote.exception.UserRelatedException;
import com.curtisnewbie.service.auth.remote.vo.*;
import com.curtisnewbie.service.auth.web.open.api.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.time.*;
import java.util.List;

import static com.curtisnewbie.common.util.AssertUtils.*;
import static com.curtisnewbie.common.util.BeanCopyUtils.mapTo;
import static com.curtisnewbie.service.auth.util.UserValidator.validatePassword;

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
    private UserWebConverter cvtr;
    @Autowired
    private AuthMessageDispatcher authMessageDispatcher;

    /**
     * Login (no role control)
     */
    @PostMapping("/login")
    public Result<String> login(@Validated @RequestBody LoginWebVo loginWebVo,
                                @RequestHeader(value = "x-forwarded-for", required = false, defaultValue = "unknown") String forwardedFor) {

        // login and generate an access token
        final String token = userService.exchangeToken(loginWebVo.getUsername(), loginWebVo.getPassword(), loginWebVo.getAppName());

        // log the access asynchronously
        final AccessLogInfoVo p = new AccessLogInfoVo();
        p.setIpAddress(forwardedFor);
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
    public Result<?> addUser(@RequestBody AddUserVo param) {
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
        resp.setList(mapTo(pps.getPayload(), cvtr::toWebInfoVo));
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

        UserRole role = null;
        UserIsDisabled isDisabled = null;

        if (param.getRole() != null) role = EnumUtils.parse(param.getRole(), UserRole.class);
        if (param.getIsDisabled() != null) isDisabled = EnumUtils.parse(param.getIsDisabled(), UserIsDisabled.class);

        if (role == null && isDisabled == null)
            return Result.error("Must have something to update, either role or is_disabled");

        userService.updateUser(UpdateUserVo.builder().id(param.getId()).isDisabled(isDisabled).role(role).updateBy(tUser.getUsername()).build());
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
        final String username = TraceUtils.tUser().getUsername();
        User user = userService.loadUserByUsername(username);
        return Result.of(BeanCopyUtils.toType(user, UserWebVo.class));
    }

    /**
     * Get user info (no role control)
     */
    @GetMapping("/detail")
    public Result<UserDetailVo> getUserDetail() {
        final String username = TraceUtils.tUser().getUsername();
        User user = userService.loadUserByUsername(username);
        UserDetailVo userDetailVo = BeanCopyUtils.toType(user, UserDetailVo.class);
        if (user.getCreateTime() != null) userDetailVo.setRegisterDate(user.getCreateTime().toLocalDate().toString());
        return Result.of(userDetailVo);
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
        if (reqVo.getRole() != null) infoVo.setRole(reqVo.getRole());
        return infoVo;
    }

}
