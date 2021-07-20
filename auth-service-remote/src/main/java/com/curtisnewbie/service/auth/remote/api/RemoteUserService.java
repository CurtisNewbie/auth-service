package com.curtisnewbie.service.auth.remote.api;

import com.curtisnewbie.service.auth.remote.exception.*;
import com.curtisnewbie.service.auth.remote.vo.RegisterUserVo;
import com.curtisnewbie.service.auth.remote.vo.UserInfoVo;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Remote service for users
 *
 * @author yongjie.zhuang
 */
@Validated
public interface RemoteUserService {

    /**
     * Login
     *
     * @param username username
     * @param password password
     * @return user's info when it was successful
     * @throws UserDisabledException      when the user is disabled
     * @throws UsernameNotFoundException  when the username is not found
     * @throws PasswordIncorrectException when the password is incorrect
     */
    @NotNull
    UserVo login(@NotEmpty String username, @NotEmpty String password) throws UserDisabledException, UsernameNotFoundException,
            PasswordIncorrectException;

    /**
     * Register user of different role
     *
     * @param registerUserVo
     * @throws UserRegisteredException        username is already registered
     * @throws ExceededMaxAdminCountException the max number of admin exceeded
     */
    void register(@NotNull RegisterUserVo registerUserVo) throws UserRegisteredException, ExceededMaxAdminCountException;

    /**
     * Update password
     *
     * @param newPassword new password (in plain text)
     * @param oldPassword old password (in plain text)
     * @param id          id
     * @throws UserNotFoundException      when the user with the given id is not found
     * @throws PasswordIncorrectException when the old password is incorrect
     */
    void updatePassword(@NotEmpty String newPassword, @NotEmpty String oldPassword, long id) throws UserNotFoundException,
            PasswordIncorrectException;

    /**
     * Fetch list of user info, excluding disabled users
     */
    @NotNull
    List<UserInfoVo> findNormalUserInfoList();

    /**
     * Fetch list of user info, including disabled users
     */
    @NotNull
    List<UserInfoVo> findAllUserInfoList();

    /**
     * Disable user by id
     *
     * @param id
     * @param disabledBy
     */
    void disableUserById(int id, @Nullable String disabledBy);

    /**
     * Enable user by id
     *
     * @param id
     * @param enabledBy
     */
    void enableUserById(int id, @Nullable String enabledBy);
}
