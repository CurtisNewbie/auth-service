package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.common.vo.*;
import com.curtisnewbie.service.auth.dao.User;
import com.curtisnewbie.service.auth.remote.vo.*;
import com.curtisnewbie.service.auth.web.open.api.vo.ListUserReq;
import com.curtisnewbie.service.auth.web.open.api.vo.UserWebVo;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;


/**
 * Service related to user table
 *
 * @author yongjie.zhuang
 */
@Validated
public interface UserService {

    /**
     * Find user by username
     *
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_NOT_FOUND
     */
    User loadUserByUsername(@NotEmpty String username);

    /**
     * Find user by username
     *
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_NOT_FOUND
     */
    UserWebVo loadUserInfo(@NotEmpty String username);

    /**
     * Login
     *
     * @param username username
     * @param password password
     * @return user's info when it was successful
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_NOT_FOUND
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#PASSWORD_INCORRECT
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_DISABLED
     */
    @NotNull
    UserVo login(@NotEmpty String username, @NotEmpty String password);

    /**
     * <p>
     * Add user (by admin)
     * </p>
     * <p>
     * This method should only be called by admin, since it doesn't require any approve/reject processes.
     * </p>
     *
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_ALREADY_REGISTERED
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#ADMIN_REG_NOT_ALLOWED
     */
    void adminAddUser(@NotNull AddUserVo addUserVo);

    /**
     * Register user
     * <p>
     * User registered here must be reviewed by the admins, only the approved user can be used
     * <p>
     * Users registered here are by default {@link UserRole#GUEST}
     * <p>
     *
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_ALREADY_REGISTERED
     */
    void register(@NotNull RegisterUserVo registerUserVo);

    /**
     * Update password
     *
     * @param newPassword new password (in plain text)
     * @param oldPassword old password (in plain text)
     * @param id          id
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_NOT_FOUND
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#PASSWORD_INCORRECT
     */
    void updatePassword(@NotEmpty String newPassword, @NotEmpty String oldPassword, long id);

    /**
     * Fetch list of user info based on the provided arguments
     */
    @NotNull
    PageableList<UserInfoVo> findUserInfoByPage(@Valid @NotNull ListUserReq vo);

    /**
     * Disable user by id
     */
    void disableUserById(int id, @Nullable String disabledBy);

    /**
     * Enable user by id
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
     * Update user
     *
     * @param param param
     */
    void updateUser(@NotNull UpdateUserVo param);

    /**
     * Delete user logically
     * <p>
     * Only the disabled user can be deleted
     * </p>
     *
     * @param userId    user's id
     * @param deletedBy deleted by
     */
    boolean deleteUserLogically(int userId, @NotEmpty String deletedBy);

    /**
     * Fetch username by ids
     *
     * @param userIds list of user_id
     * @return user_id -> username map
     */
    Map<Integer, String> fetchUsernameById(@NotNull List<Integer> userIds);

    /**
     * Check if the password (not secret key / user key) is correct
     */
    boolean validateUserPassword(@NotBlank String username, @NotBlank String password);

    /**
     * Exchange JWT token
     *
     * @param username username
     * @param password password in plaintext
     * @return JWT Token
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_NOT_FOUND
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#PASSWORD_INCORRECT
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_DISABLED
     */
    String exchangeToken(@NotEmpty String username, @NotEmpty String password);

    /**
     * Exchange JWT token
     *
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#TOKEN_EXPIRED
     */
    String exchangeToken(@NotEmpty String token);

    /**
     * Get user info
     * <p>
     * the retured User object will not contain password value
     *
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#TOKEN_EXPIRED
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_NOT_FOUND
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_DISABLED
     */
    User getUserInfoByToken(@NotEmpty String token);

    /**
     * Review user registration
     */
    void reviewUserRegistration(@NotNull UserReviewCmd cmd);

    /** Get User info by id */
    UserInfoVo getUserInfo(int userId);

    /** Get User info by userNo */
    UserInfoVo getUserInfo(@NotEmpty String userNo);

    /**
     * Fetch usernames by userNos
     */
    Map<String, String> fetchUsernameByUserNos(@NotEmpty List<String> userNos);

    /** Try to find user by id/userNo/name */
    UserInfoVo findUser(@NotNull FindUserReq req);
}
