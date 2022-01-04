package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.remote.api.RemoteEventHandlingService;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.exception.*;
import com.curtisnewbie.service.auth.remote.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author yongjie.zhuang
 */
@RequestMapping("/remote/user")
@FeignClient(FeignConst.SERVICE_NAME)
public interface UserServiceFeign {

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
    @PostMapping("/login")
    Result<UserVo> login(@RequestParam("username") String username, @RequestParam("password") String password)
            throws UserDisabledException, UsernameNotFoundException, PasswordIncorrectException;

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
    @PostMapping("/login-with-app")
    Result<UserVo> login(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("appName") String appName)
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
    @PostMapping("/register")
    Result<Void> register(@RequestBody RegisterUserVo registerUserVo) throws UserRegisteredException, ExceededMaxAdminCountException;

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
    @PostMapping("/registration/request-approval")
    Result<Void> requestRegistrationApproval(@RequestBody RegisterUserVo registerUserVo) throws UserRegisteredException,
            ExceededMaxAdminCountException;

    /**
     * Update password
     *
     * @param newPassword new password (in plain text)
     * @param oldPassword old password (in plain text)
     * @param userId      user's id
     * @throws UserNotFoundException      when the user with the given id is not found
     * @throws PasswordIncorrectException when the old password is incorrect
     */
    @PostMapping("/password/udpate")
    Result<Void> updatePassword(@RequestParam("newPassword") String newPassword, @RequestParam("oldPassword") String oldPassword,
                                @RequestParam("userId") long userId) throws UserNotFoundException, PasswordIncorrectException;


    /**
     * Fetch list of user info based on the provided arguments
     */
    @PostMapping("/list")
    Result<PageablePayloadSingleton<List<UserInfoVo>>> findUserInfoByPage(@RequestBody FindUserInfoVo vo);

    /**
     * Disable user by id
     *
     * @param id
     * @param disabledBy
     */
    @PostMapping("/disable")
    Result<Void> disableUserById(@RequestParam("id") int id, @RequestParam(value = "disabledBy", defaultValue = "") String disabledBy);

    /**
     * Enable user by id
     *
     * @param id
     * @param enabledBy
     */
    @PostMapping("/enable")
    Result<Void> enableUserById(@RequestParam("id") int id, @RequestParam(value = "enabledBy", defaultValue = "") String enabledBy);

    /**
     * Find username by id
     */
    @GetMapping("/username")
    Result<String> findUsernameById(@RequestParam("id") int id);

    /**
     * Find id by username
     */
    @GetMapping("/id")
    Result<Integer> findIdByUsername(@RequestParam("username") String username);

    /**
     * Change user's role and enable the user
     */
    @PostMapping("/change-role-and-enable")
    Result<Void> changeRoleAndEnableUser(@RequestParam("userId") int userId, @RequestParam("role") UserRole role,
                                         @RequestParam(value = "updatedBy", defaultValue = "") String updatedBy);

    /**
     * Update user
     *
     * @param param param
     */
    @PostMapping("/update")
    Result<Void> updateUser(@RequestBody UpdateUserVo param);

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
    @DeleteMapping
    Result<Void> deleteUser(@RequestParam("userId") int userId, @RequestParam(value = "deletedBy", defaultValue = "") String deletedBy);

    /**
     * Update user role
     */
    @PostMapping("/role/update")
    Result<Void> updateRole(@RequestParam("id") int id, @RequestParam("role") UserRole role,
                            @RequestParam(value = "updatedBy", defaultValue = "") String updatedBy);

    /**
     * Fetch username by ids
     *
     * @param userIds list of user_id
     * @return user_id -> username map
     */
    @PostMapping("/username")
    Result<Map<Integer, String>> fetchUsernameById(@RequestBody List<Integer> userIds);
}
