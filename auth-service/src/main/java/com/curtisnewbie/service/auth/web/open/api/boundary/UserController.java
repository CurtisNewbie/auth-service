package com.curtisnewbie.service.auth.web.open.api.boundary;

import com.curtisnewbie.common.advice.RoleRequired;
import com.curtisnewbie.common.trace.TUser;
import com.curtisnewbie.common.trace.TraceUtils;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.dao.User;
import com.curtisnewbie.service.auth.infrastructure.converters.UserWebConverter;
import com.curtisnewbie.service.auth.local.api.LocalUserService;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.exception.InvalidAuthenticationException;
import com.curtisnewbie.service.auth.remote.exception.UserRelatedException;
import com.curtisnewbie.service.auth.remote.vo.*;
import com.curtisnewbie.service.auth.web.open.api.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private LocalUserService userService;

    @Autowired
    private UserWebConverter cvtr;

    /**
     * Login (no role control)
     */
    @PostMapping("/login")
    public Result<String> login(@Validated @RequestBody LoginWebVo loginWebVo) throws UserRelatedException {
        return Result.of(userService.exchangeToken(loginWebVo.getUsername(), loginWebVo.getPassword()));
    }

    /**
     * Add User (Only Admin is permitted)
     */
    @RoleRequired(role = "admin")
    @PostMapping("/add")
    public Result<?> addUser(@RequestBody AddUserVo param) {
        userService.addUser(param);
        return Result.ok();
    }

    /**
     * Registration request (no role control)
     */
    @PostMapping("/register/request")
    public Result<?> register(@RequestBody RegisterUserVo vo) {
        userService.register(vo);
        return Result.ok();
    }

    /**
     * List users (only admin)
     */
    @RoleRequired(role = "admin")
    @PostMapping("/list")
    public Result<GetUserListRespWebVo> getUserList(@RequestBody GetUserListReqWebVo reqVo) {
        FindUserInfoVo searchParam = toFindUserInfoVo(reqVo);
        PageablePayloadSingleton<List<UserInfoVo>> pps = userService.findUserInfoByPage(searchParam);
        GetUserListRespWebVo resp = new GetUserListRespWebVo();
        resp.setList(mapTo(pps.getPayload(), cvtr::toWebInfoVo));
        resp.setPagingVo(pps.getPagingVo());
        return Result.of(resp);
    }

    /**
     * Delete user logically (only admin)
     */
    @RoleRequired(role = "admin")
    @PostMapping("/delete")
    public Result<Void> deleteUser(@RequestBody DeleteUserReqWebVo reqVo) throws InvalidAuthenticationException {
        TUser tUser = TraceUtils.tUser();
        Assert.isTrue(UserRole.isAdmin(tUser.getRole()), "Not permitted");

        userService.deleteUserLogically(reqVo.getId(), tUser.getUsername());
        return Result.ok();
    }

    /**
     * Change user's role (only admin)
     */
    @RoleRequired(role = "admin")
    @PostMapping("/info/update")
    public Result<Void> changeUserRole(@RequestBody UpdateUserInfoReqVo param) {
        TUser tUser = TraceUtils.tUser();
        Assert.isTrue(UserRole.isAdmin(tUser.getRole()), "Not permitted");

        nonNull(param.getId(), "id == null");
        notEquals(param.getId(), TraceUtils.tUser().getUserId(), "You cannot update yourself");

        UserRole role = null;
        UserIsDisabled isDisabled = null;

        if (param.getRole() != null)
            role = EnumUtils.parse(param.getRole(), UserRole.class);
        if (param.getIsDisabled() != null)
            isDisabled = EnumUtils.parse(param.getIsDisabled(), UserIsDisabled.class);

        if (role == null && isDisabled == null)
            return Result.error("Must have something to update, either role or is_disabled");

        userService.updateUser(UpdateUserVo.builder()
                .id(param.getId())
                .isDisabled(isDisabled)
                .role(role)
                .updateBy(tUser.getUsername())
                .build());
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
        if (user.getCreateTime() != null)
            userDetailVo.setRegisterDate(user.getCreateTime().toLocalDate().toString());
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
        if (reqVo.getIsDisabled() != null)
            infoVo.setIsDisabled(reqVo.getIsDisabled());
        if (reqVo.getRole() != null)
            infoVo.setRole(reqVo.getRole());
        return infoVo;
    }

}
