package com.curtisnewbie.service.auth.api;

import com.curtisnewbie.service.auth.exception.*;
import com.curtisnewbie.service.auth.vo.RegisterUserVo;
import com.curtisnewbie.service.auth.vo.UserInfoVo;
import com.curtisnewbie.service.auth.vo.UserVo;

import java.util.List;

/**
 * Remote service for users
 *
 * @author yongjie.zhuang
 */
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
    UserVo login(String username, String password) throws UserDisabledException, UsernameNotFoundException,
            PasswordIncorrectException;

    /**
     * Register user of different role
     *
     * @param registerUserVo
     * @throws UserRegisteredException        username is already registered
     * @throws ExceededMaxAdminCountException the max number of admin exceeded
     */
    void register(RegisterUserVo registerUserVo) throws UserRegisteredException, ExceededMaxAdminCountException;

    /**
     * Update password
     *
     * @param newPassword new password (in plain text)
     * @param oldPassword old password (in plain text)
     * @param id          id
     * @throws UserNotFoundException      when the user with the given id is not found
     * @throws PasswordIncorrectException when the old password is incorrect
     */
    void updatePassword(String newPassword, String oldPassword, long id) throws UserNotFoundException,
            PasswordIncorrectException;

    /**
     * Fetch list of user info, excluding disabled users
     */
    List<UserInfoVo> findNormalUserInfoList();

    /**
     * Fetch list of user info, including disabled users
     */
    List<UserInfoVo> findAllUserInfoList();

    /**
     * Disable user by id
     *
     * @param id
     * @param disabledBy
     */
    void disableUserById(int id, String disabledBy);

    /**
     * Enable user by id
     *
     * @param id
     * @param enabledBy
     */
    void enableUserById(int id, String enabledBy);
}
