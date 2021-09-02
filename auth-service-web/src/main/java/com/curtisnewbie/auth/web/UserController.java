package com.curtisnewbie.auth.web;

import com.curtisnewbie.auth.vo.*;
import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.common.util.ValidUtils;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.aop.LogOperation;
import com.curtisnewbie.module.auth.util.AuthUtil;
import com.curtisnewbie.service.auth.remote.api.RemoteUserService;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.exception.InvalidAuthenticationException;
import com.curtisnewbie.service.auth.remote.exception.UserRelatedException;
import com.curtisnewbie.service.auth.remote.vo.FindUserInfoVo;
import com.curtisnewbie.service.auth.remote.vo.RegisterUserVo;
import com.curtisnewbie.service.auth.remote.vo.UserInfoVo;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author yongjie.zhuang
 */
@RestController
@RequestMapping("${web.base-path}/user")
public class UserController {

    private static final int PASSWORD_LENGTH = 6;

    @DubboReference(lazy = true)
    private RemoteUserService userService;

    @LogOperation(name = "/user/register", description = "add user")
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/register")
    public Result<?> addUser(@RequestBody RegisterUserWebVo registerUserVo) throws UserRelatedException,
            MsgEmbeddedException {
        RegisterUserVo vo = new RegisterUserVo();
        BeanUtils.copyProperties(registerUserVo, vo);

        // validate whether username and password is entered
        ValidUtils.requireNotEmpty(vo.getUsername(), "Please enter username");
        ValidUtils.requireNotEmpty(vo.getPassword(), "Please enter password");

        // validate if the username and password is the same
        ValidUtils.requireNotEquals(vo.getUsername(), vo.getPassword(), "Username and password must be different");

        // validate if the password is too short
        if (vo.getPassword().length() < PASSWORD_LENGTH)
            return Result.error("Password must have at least " + PASSWORD_LENGTH + " characters");

        // if not specified, the role will be guest
        UserRole role = UserRole.GUEST;
        if (registerUserVo.getUserRole() != null) {
            role = UserRole.parseUserRole(registerUserVo.getUserRole());
            ValidUtils.requireNonNull(role, "Illegal user role");
        }
        // do not support adding administrator
        if (Objects.equals(role, UserRole.ADMIN)) {
            return Result.error("Do not support adding administrator");
        }
        vo.setRole(role);
        vo.setCreateBy(AuthUtil.getUsername());
        userService.register(vo);
        return Result.ok();
    }

    @LogOperation(name = "/user/register/request", description = "User request's registration approval")
    @PostMapping("/register/request")
    public Result<?> requestRegistration(@RequestBody RequestRegisterUserWebVo registerUserVo) throws UserRelatedException,
            MsgEmbeddedException {
        RegisterUserVo vo = new RegisterUserVo();
        BeanUtils.copyProperties(registerUserVo, vo);

        // validate whether username and password is entered
        ValidUtils.requireNotEmpty(vo.getUsername(), "Please enter username");
        ValidUtils.requireNotEmpty(vo.getPassword(), "Please enter password");

        // validate if the username and password are the same
        ValidUtils.requireNotEquals(vo.getUsername(), vo.getPassword(), "Username and password must be different");

        // validate if the password is too short
        if (vo.getPassword().length() < PASSWORD_LENGTH)
            return Result.error("Password must have at least " + PASSWORD_LENGTH + " characters");

        // by default role is guest
        vo.setRole(UserRole.GUEST);
        // created by this user himself/herself
        vo.setCreateBy(vo.getUsername());

        userService.requestRegistrationApproval(vo);
        return Result.ok();
    }

    @LogOperation(name = "/user/list", description = "get user list")
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/list")
    public Result<GetUserListRespVo> getUserList(@RequestBody GetUserListReqVo reqVo) {
        FindUserInfoVo searchParam = toFindUserInfoVo(reqVo);
        PageInfo<UserInfoVo> voPageInfo = userService.findUserInfoByPage(searchParam);
        GetUserListRespVo resp = new GetUserListRespVo(BeanCopyUtils.toTypeList(voPageInfo.getList(), UserInfoWebVo.class));
        resp.setPagingVo(new PagingVo().ofTotal(voPageInfo.getTotal()));
        return Result.of(resp);
    }

    private static FindUserInfoVo toFindUserInfoVo(GetUserListReqVo reqVo) {
        FindUserInfoVo infoVo = new FindUserInfoVo();
        infoVo.setUsername(reqVo.getUsername());
        infoVo.setPagingVo(reqVo.getPagingVo());
        if (reqVo.getIsDisabled() != null)
            infoVo.setIsDisabled(EnumUtils.parse(reqVo.getIsDisabled(), UserIsDisabled.class));
        if (reqVo.getRole() != null)
            infoVo.setRole(EnumUtils.parse(reqVo.getRole(), UserRole.class));
        return infoVo;
    }

    @LogOperation(name = "/user/disable", description = "disable user")
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/disable")
    public Result<Void> disableUserById(@RequestBody DisableUserById param) throws MsgEmbeddedException, InvalidAuthenticationException {
        ValidUtils.requireNonNull(param.getId());
        if (Objects.equals(param.getId(), AuthUtil.getUser().getId())) {
            throw new MsgEmbeddedException("You cannot disable yourself");
        }
        userService.disableUserById(param.getId(), AuthUtil.getUsername());
        return Result.ok();
    }

    @LogOperation(name = "/user/enable", description = "enable user")
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/enable")
    public Result<Void> enableUserById(@RequestBody DisableUserById param) throws MsgEmbeddedException, InvalidAuthenticationException {
        ValidUtils.requireNonNull(param.getId());
        if (Objects.equals(param.getId(), AuthUtil.getUser().getId())) {
            throw new MsgEmbeddedException("You cannot enable yourself");
        }
        userService.enableUserById(param.getId(), AuthUtil.getUsername());
        return Result.ok();
    }

    @LogOperation(name = "/user/role/change", description = "change user role")
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/role/change")
    public Result<Void> changeUserRole(@RequestBody ChangeUserRoleReqVo param) throws MsgEmbeddedException, InvalidAuthenticationException {
        ValidUtils.requireNonNull(param.getId());
        if (Objects.equals(param.getId(), AuthUtil.getUser().getId())) {
            throw new MsgEmbeddedException("You cannot update yourself");
        }
        UserRole role = EnumUtils.parse(param.getRole(), UserRole.class);
        ValidUtils.requireNonNull(role, "user_role value illegal");
        userService.updateRole(param.getId(), role, AuthUtil.getUsername());
        return Result.ok();
    }

    @LogOperation(name = "/user/info", description = "get user info", enabled = false)
    @GetMapping("/info")
    public Result<UserWebVo> getUserInfo() throws InvalidAuthenticationException {
        // user is not authenticated yet
        if (!AuthUtil.isPrincipalPresent(UserVo.class)) {
            return Result.ok();
        }
        UserVo ue = AuthUtil.getUser();
        return Result.of(BeanCopyUtils.toType(ue, UserWebVo.class));
    }

    @LogOperation(name = "/user/password/update", description = "update password")
    @PostMapping("/password/update")
    public Result<Void> updatePassword(@RequestBody UpdatePasswordVo vo) throws MsgEmbeddedException, InvalidAuthenticationException {
        ValidUtils.requireNotEmpty(vo.getNewPassword());
        ValidUtils.requireNotEmpty(vo.getPrevPassword());

        // check if the old password and prev password are equal
        ValidUtils.requireNotEquals(vo.getNewPassword(), vo.getPrevPassword(), "New password must be different");

        // validate if the new password is too short
        if (vo.getNewPassword().length() < PASSWORD_LENGTH)
            return Result.error("Password must have at least " + PASSWORD_LENGTH + " characters");

        UserVo uv = AuthUtil.getUser();
        try {
            userService.updatePassword(vo.getNewPassword(), vo.getPrevPassword(), uv.getId());
        } catch (UserRelatedException ignore) {
            return Result.error("Password incorrect");
        }
        return Result.ok();
    }
}
