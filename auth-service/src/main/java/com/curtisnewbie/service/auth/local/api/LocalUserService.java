package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.service.auth.dao.User;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.exception.*;
import com.curtisnewbie.service.auth.remote.vo.*;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;


/**
 * Service related to user table
 *
 * @author yongjie.zhuang
 */
public interface LocalUserService {

    /**
     * Find user by username
     *
     * @throws UsernameNotFoundException user with given username is not found
     */
    User loadUserByUsername(@NotEmpty String username) throws UsernameNotFoundException;

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
     * Login
     * </p>
     * <p>
     * Different from {@link #login(String, String)}, this method takes into the consideration of applications that the
     * user is allowed to use.
     * </p>
     *
     * @param username username
     * @param password password
     * @param appName  application name that the user is trying to use
     * @return user's info when it was successful
     * @throws UserDisabledException                   when the user is disabled
     * @throws UsernameNotFoundException               when the username is not found
     * @throws PasswordIncorrectException              when the password is incorrect
     * @throws UserNotAllowedToUseApplicationException when the user is not allowed to use this application
     */
    @NotNull
    UserVo login(@NotEmpty String username, @NotEmpty String password, @NotEmpty String appName)
            throws UserDisabledException, UsernameNotFoundException, PasswordIncorrectException, UserNotAllowedToUseApplicationException;

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
    PageablePayloadSingleton<List<UserInfoVo>> findUserInfoByPage(@Valid @NotNull FindUserInfoVo vo);

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
     * Find id by username
     */
    Integer findIdByUsername(@NotEmpty String username);

    /**
     * Change user's role and enable the user
     */
    void changeRoleAndEnableUser(int userId, @NotNull UserRole role, @Nullable String updatedBy);

    /**
     * Update user
     *
     * @param param param
     */
    void updateUser(@NotNull UpdateUserVo param);

    /**
     * Delete user
     * <p>
     * Move user to table 'deleted_user'
     * </p>
     * <p>
     * Only the disabled user can be deleted
     * </p>
     *
     * @param userId    user's id
     * @param deletedBy deleted by
     */
    void deleteUser(int userId, @NotEmpty String deletedBy);

    /**
     * Update user role
     */
    void updateRole(int id, @NotNull UserRole role, @Nullable String updatedBy);

    /**
     * Fetch username by ids
     *
     * @param userIds list of user_id
     * @return user_id -> username map
     */
    Map<Integer, String> fetchUsernameById(List<Integer> userIds);
}
