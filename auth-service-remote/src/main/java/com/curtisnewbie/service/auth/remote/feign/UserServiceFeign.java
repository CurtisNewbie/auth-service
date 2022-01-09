package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.remote.exception.*;
import com.curtisnewbie.service.auth.remote.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author yongjie.zhuang
 */
@FeignClient(value = FeignConst.SERVICE_NAME, path = UserServiceFeign.PATH)
public interface UserServiceFeign {

    String PATH = "/remote/user";

    /**
     * Login
     *
     * @return user's info when it was successful
     * @throws UserDisabledException      when the user is disabled
     * @throws UsernameNotFoundException  when the username is not found
     * @throws PasswordIncorrectException when the password is incorrect
     */
    @PostMapping(value = "/login")
    Result<UserVo> login(@Validated @RequestBody LoginVo vo) throws UserDisabledException, UsernameNotFoundException, PasswordIncorrectException;

    /**
     * <p>
     * Login
     * </p>
     * <p>
     * Different from {@link #loginForApp(LoginVo)}, this method takes into the consideration of applications that the
     * user is allowed to use.
     * </p>
     *
     * @return user's info when it was successful
     * @throws UserDisabledException                   when the user is disabled
     * @throws UsernameNotFoundException               when the username is not found
     * @throws PasswordIncorrectException              when the password is incorrect
     * @throws UserNotAllowedToUseApplicationException when the user is not allowed to use this application
     */
    @PostMapping("/login-with-app")
    Result<UserVo> loginForApp(@Validated @RequestBody LoginVo vo) throws UserDisabledException, UsernameNotFoundException, PasswordIncorrectException,
            UserNotAllowedToUseApplicationException;

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
    Result<Void> register(@Validated @RequestBody RegisterUserVo registerUserVo) throws UserRegisteredException, ExceededMaxAdminCountException;

    /**
     * <p>
     * Register user of different role
     * </p>
     * <p>
     * User registered with this method is disabled by default, it requires the admin to 'approve' the registration by
     * enabling it. To do this, this method will generate a {@code event_handling} record, that will later be received
     * by the admin and handled. For more information, see {@link EventHandlingServiceFeign}
     * </p>
     *
     * @param registerUserVo
     * @throws UserRegisteredException        username is already registered
     * @throws ExceededMaxAdminCountException the max number of admin exceeded
     * @see
     */
    @PostMapping("/registration/request-approval")
    Result<Void> requestRegistrationApproval(@Validated @RequestBody RegisterUserVo registerUserVo) throws UserRegisteredException,
            ExceededMaxAdminCountException;

    /**
     * Update password
     *
     * @throws UserNotFoundException      when the user with the given id is not found
     * @throws PasswordIncorrectException when the old password is incorrect
     */
    @PostMapping("/password/udpate")
    Result<Void> updatePassword(@Validated @RequestBody UpdatePasswordVo vo) throws UserNotFoundException, PasswordIncorrectException;


    /**
     * Fetch list of user info based on the provided arguments
     */
    @PostMapping("/list")
    Result<PageablePayloadSingleton<List<UserInfoVo>>> findUserInfoByPage(@RequestBody FindUserInfoVo vo);

    /**
     * Disable user by id
     */
    @PostMapping(path = "/disable")
    Result<Void> disableUserById(@RequestBody DisableUserByIdCmd req);

    /**
     * Enable user by id
     */
    @PostMapping(path = "/enable")
    Result<Void> enableUserById(@RequestBody EnableUserByIdCmd cmd);

    /**
     * Find username by id
     */
    @GetMapping("/username")
    Result<String> findUsernameById(@RequestParam("id") int id);

    /**
     * Find id by username
     */
    @GetMapping("/id")
    Result<Integer> findIdByUsername(@NotBlank @RequestParam("username") String username);

    /**
     * Change user's role and enable the user
     */
    @PostMapping("/change-role-and-enable")
    Result<Void> changeRoleAndEnableUser(@Validated @RequestBody ChangeRoleAndEnableUserCmd cmd);

    /**
     * Update user
     *
     * @param param param
     */
    @PostMapping("/update")
    Result<Void> updateUser(@Validated @RequestBody UpdateUserVo param);

    /**
     * Delete user
     * <p>
     * Move user to table 'deleted_user'
     * </p>
     * <p>
     * Only the disabled user can be deleted
     * </p>
     */
    @DeleteMapping
    Result<Void> deleteUser(@RequestBody DeleteUserCmd cmd);

    /**
     * Update user role
     */
    @PostMapping(path = "/role/update")
    Result<Void> updateRole(@Validated @RequestBody UpdateRoleCmd cmd);

    /**
     * Fetch username by ids
     *
     * @return user_id -> username map
     */
    @PostMapping(value = "/username")
    Result<FetchUsernameByIdResp> fetchUsernameById(@Validated @RequestBody FetchUsernameByIdReq req);
}
