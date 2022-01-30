package com.curtisnewbie.service.auth.web.open.api.boundary;

import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.common.util.ValidUtils;
import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.aop.LogOperation;
import com.curtisnewbie.module.auth.util.AuthUtil;
import com.curtisnewbie.service.auth.infrastructure.converters.UserWebConverter;
import com.curtisnewbie.service.auth.local.api.LocalUserService;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.exception.InvalidAuthenticationException;
import com.curtisnewbie.service.auth.remote.exception.UserRelatedException;
import com.curtisnewbie.service.auth.remote.vo.*;
import com.curtisnewbie.service.auth.web.open.api.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.curtisnewbie.common.util.BeanCopyUtils.mapTo;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@RestController
@RequestMapping("${web.base-path}/user")
public class UserController {

    private static final int PASSWORD_LENGTH = 6;

    @Autowired
    private LocalUserService userService;

    @Autowired
    private UserWebConverter cvtr;

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
            role = registerUserVo.getUserRole();
        }
        // todo do not support adding administrator
        if (role == UserRole.ADMIN) {
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

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/list")
    public Result<GetUserListRespWebVo> getUserList(@RequestBody GetUserListReqWebVo reqVo) {
        FindUserInfoVo searchParam = toFindUserInfoVo(reqVo);
        PageablePayloadSingleton<List<UserInfoVo>> pps = userService.findUserInfoByPage(searchParam);
        GetUserListRespWebVo resp = new GetUserListRespWebVo();
        resp.setList(mapTo(pps.getPayload(), cvtr::toWebInfoVo));
        resp.setPagingVo(pps.getPagingVo());
        return Result.of(resp);
    }

    @LogOperation(name = "/user/delete", description = "delete user")
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/delete")
    public Result<Void> deleteUser(@RequestBody DeleteUserReqWebVo reqVo) throws InvalidAuthenticationException {
        final String deletedBy = AuthUtil.getUsername();
        log.info("Delete user {} by {}", reqVo.getId(), deletedBy);
        userService.deleteUser(reqVo.getId(), deletedBy);
        return Result.ok();
    }


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

    @LogOperation(name = "/user/info/update", description = "update user info")
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/info/update")
    public Result<Void> changeUserRole(@RequestBody UpdateUserInfoReqVo param) throws MsgEmbeddedException, InvalidAuthenticationException {
        ValidUtils.requireNonNull(param.getId());
        if (Objects.equals(param.getId(), AuthUtil.getUser().getId())) {
            throw new MsgEmbeddedException("You cannot update yourself");
        }

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
                .updateBy(AuthUtil.getUsername())
                .build());
        return Result.ok();
    }

    @GetMapping("/info")
    public Result<UserWebVo> getUserInfo() throws InvalidAuthenticationException {
        // user is not authenticated yet
        Optional<UserVo> optionalUser = AuthUtil.getOptionalUser();
        if (!optionalUser.isPresent()) {
            return Result.ok();
        }
        UserVo ue = optionalUser.get();
        return Result.of(BeanCopyUtils.toType(ue, UserWebVo.class));
    }

    @LogOperation(name = "/user/password/update", description = "update password")
    @PostMapping("/password/update")
    public Result<Void> updatePassword(@RequestBody UpdatePasswordWebVo vo) throws MsgEmbeddedException, InvalidAuthenticationException {
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
