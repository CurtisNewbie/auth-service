package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.local.api.LocalUserService;
import com.curtisnewbie.service.auth.remote.exception.*;
import com.curtisnewbie.service.auth.remote.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yongjie.zhuang
 */
@RequestMapping(value = UserServiceFeign.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class UserServiceFeignController implements UserServiceFeign {

    @Autowired
    private LocalUserService localUserService;

    @Override
    public Result<UserVo> login(LoginVo loginVo) throws UserDisabledException, UsernameNotFoundException,
            PasswordIncorrectException {

        return Result.of(localUserService.login(loginVo.getUsername(), loginVo.getPassword()));
    }

    @Override
    public Result<UserVo> loginForApp(LoginVo loginVo) throws UserDisabledException, UsernameNotFoundException,
            PasswordIncorrectException, UserNotAllowedToUseApplicationException {

        return Result.of(localUserService.login(loginVo.getUsername(), loginVo.getPassword(), loginVo.getAppName()));
    }

    @Override
    public Result<Void> register(RegisterUserVo registerUserVo) throws UserRegisteredException, ExceededMaxAdminCountException {
        localUserService.register(registerUserVo);
        return Result.ok();
    }

    @Override
    public Result<Void> requestRegistrationApproval(RegisterUserVo registerUserVo) throws UserRegisteredException,
            ExceededMaxAdminCountException {
        localUserService.requestRegistrationApproval(registerUserVo);
        return Result.ok();
    }

    @Override
    public Result<Void> updatePassword(UpdatePasswordVo vo) throws UserNotFoundException,
            PasswordIncorrectException {
        localUserService.updatePassword(vo.getNewPassword(), vo.getOldPassword(), vo.getUserId());
        return Result.ok();
    }

    @Override
    public Result<PageablePayloadSingleton<List<UserInfoVo>>> findUserInfoByPage(FindUserInfoVo vo) {
        return Result.of(localUserService.findUserInfoByPage(vo));
    }

    @Override
    public Result<Void> disableUserById(DisableUserByIdCmd cmd) {
        localUserService.disableUserById(cmd.getId(), cmd.getDisabledBy());
        return Result.ok();
    }

    @Override
    public Result<Void> enableUserById(EnableUserByIdCmd cmd) {
        localUserService.enableUserById(cmd.getId(), cmd.getEnabledBy());
        return Result.ok();
    }

    @Override
    public Result<String> findUsernameById(int id) {
        localUserService.findUsernameById(id);
        return null;
    }

    @Override
    public Result<Integer> findIdByUsername(String username) {
        return Result.of(localUserService.findIdByUsername(username));
    }

    @Override
    public Result<Void> changeRoleAndEnableUser(ChangeRoleAndEnableUserCmd cmd) {
        localUserService.changeRoleAndEnableUser(cmd.getUserId(), cmd.getRole(), cmd.getUpdatedBy());
        return Result.ok();

    }

    @Override
    public Result<Void> updateUser(UpdateUserVo param) {
        localUserService.updateUser(param);
        return Result.ok();
    }

    @Override
    public Result<Void> deleteUser(DeleteUserCmd cmd) {
        localUserService.deleteUser(cmd.getUserId(), cmd.getDeletedBy());
        return Result.ok();
    }

    @Override
    public Result<Void> updateRole(UpdateRoleCmd cmd) {
        localUserService.updateRole(cmd.getId(), cmd.getRole(), cmd.getUpdatedBy());
        return Result.ok();
    }

    @Override
    public Result<FetchUsernameByIdResp> fetchUsernameById(FetchUsernameByIdReq req) {
        return Result.of(FetchUsernameByIdResp.builder()
                .idToUsername(localUserService.fetchUsernameById(req.getUserIds()))
                .build());
    }
}
