package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.local.api.LocalUserService;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.exception.*;
import com.curtisnewbie.service.auth.remote.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author yongjie.zhuang
 */
@RestController
public class UserServiceController implements UserServiceFeign {

    @Autowired
    private LocalUserService localUserService;

    @Override
    public Result<UserVo> login(String username, String password) throws UserDisabledException, UsernameNotFoundException,
            PasswordIncorrectException {

        return Result.of(localUserService.login(username, password));
    }

    @Override
    public Result<UserVo> login(String username, String password, String appName) throws UserDisabledException, UsernameNotFoundException,
            PasswordIncorrectException, UserNotAllowedToUseApplicationException {

        return Result.of(localUserService.login(username, password, appName));
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
    public Result<Void> updatePassword(String newPassword, String oldPassword, long userId) throws UserNotFoundException,
            PasswordIncorrectException {
        localUserService.updatePassword(newPassword, oldPassword, userId);
        return Result.ok();
    }

    @Override
    public Result<PageablePayloadSingleton<List<UserInfoVo>>> findUserInfoByPage(FindUserInfoVo vo) {
        return Result.of(localUserService.findUserInfoByPage(vo));
    }

    @Override
    public Result<Void> disableUserById(int id, String disabledBy) {
        localUserService.disableUserById(id, disabledBy);
        return Result.ok();
    }

    @Override
    public Result<Void> enableUserById(int id, String enabledBy) {
        localUserService.enableUserById(id, enabledBy);
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
    public Result<Void> changeRoleAndEnableUser(int userId, UserRole role, String updatedBy) {
        localUserService.changeRoleAndEnableUser(userId, role, updatedBy);
        return Result.ok();

    }

    @Override
    public Result<Void> updateUser(UpdateUserVo param) {
        localUserService.updateUser(param);
        return Result.ok();
    }

    @Override
    public Result<Void> deleteUser(int userId, String deletedBy) {
        localUserService.deleteUser(userId, deletedBy);
        return Result.ok();
    }

    @Override
    public Result<Void> updateRole(int id, UserRole role, String updatedBy) {
        localUserService.updateRole(id, role, updatedBy);
        return Result.ok();
    }

    @Override
    public Result<Map<Integer, String>> fetchUsernameById(List<Integer> userIds) {
        return Result.of(localUserService.fetchUsernameById(userIds));
    }
}
