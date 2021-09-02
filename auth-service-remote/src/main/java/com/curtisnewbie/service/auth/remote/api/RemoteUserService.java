package com.curtisnewbie.service.auth.remote.api;

import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.exception.*;
import com.curtisnewbie.service.auth.remote.vo.FindUserInfoVo;
import com.curtisnewbie.service.auth.remote.vo.RegisterUserVo;
import com.curtisnewbie.service.auth.remote.vo.UserInfoVo;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import com.github.pagehelper.PageInfo;
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
     * <p>
     * Register user of different role
     * </p>
     * <p>
     * This method should only be called by admin, since it doesn't require any approve/reject processes.
     * </p>
     *
     * @param registerUserVo
     * @throws UserRegisteredException        username is already registered
     * @throws ExceededMaxAdminCountException the max number of admin exceeded
     * @see
     */
    void register(@NotNull RegisterUserVo registerUserVo) throws UserRegisteredException, ExceededMaxAdminCountException;

    /**
     * <p>
     * Register user of different role
     * </p>
     * <p>
     * User registered with this method is disabled by default, it requires the admin to 'approve' the registration by
     * enabling it. To do this, this method will generate a {@code event_handling} record, that will later be received
     * by the admin and handled. For more information, see {@link RemoteEventHandlingService}
     * </p>
     *
     * @param registerUserVo
     * @throws UserRegisteredException        username is already registered
     * @throws ExceededMaxAdminCountException the max number of admin exceeded
     * @see
     */
    void requestRegistrationApproval(@NotNull RegisterUserVo registerUserVo) throws UserRegisteredException,
            ExceededMaxAdminCountException;

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
     * Fetch list of user info based on the provided arguments
     */
    @NotNull
    PageInfo<UserInfoVo> findUserInfoByPage(@NotNull FindUserInfoVo vo);

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


    /**
     * Find username by id
     */
    String findUsernameById(int id);

    /**
     * Change user's role and enable the user
     */
    void changeRoleAndEnableUser(int userId, @NotNull UserRole role, @Nullable String updatedBy);

    /**
     * Update user role
     */
    void updateRole(int id, @NotNull UserRole role, @Nullable String updatedBy);
}
