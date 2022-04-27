package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.remote.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Feign for User
 *
 * @author yongjie.zhuang
 */
@FeignClient(value = FeignConst.SERVICE_NAME, path = UserServiceFeign.PATH)
public interface UserServiceFeign {

    String PATH = "/remote/user";

    /**
     * Login
     *
     * @return user's info when it was successful
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_NOT_FOUND
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#PASSWORD_INCORRECT
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_DISABLED
     */
    @PostMapping(value = "/login")
    Result<UserVo> login(@Validated @RequestBody LoginVo vo);

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
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_NOT_FOUND
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#PASSWORD_INCORRECT
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_DISABLED
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_NOT_PERMITTED
     */
    @PostMapping("/login-with-app")
    Result<UserVo> loginForApp(@Validated @RequestBody LoginVo vo);

    /**
     * <p>
     * Register user of different role
     * </p>
     * <p>
     * This method should only be called by admin, since it doesn't require any approve/reject processes.
     * </p>
     *
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_ALREADY_REGISTERED
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#ADMIN_REG_NOT_ALLOWED
     */
    @PostMapping("/register")
    Result<Void> register(@Validated @RequestBody RegisterUserVo registerUserVo);

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
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_ALREADY_REGISTERED
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#ADMIN_REG_NOT_ALLOWED
     */
    @PostMapping("/registration/request-approval")
    Result<Void> requestRegistrationApproval(@Validated @RequestBody RegisterUserVo registerUserVo);

    /**
     * Update password
     *
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_NOT_FOUND
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#PASSWORD_INCORRECT
     */
    @PostMapping("/password/udpate")
    Result<Void> updatePassword(@Validated @RequestBody UpdatePasswordVo vo);


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
     * Delete user (logically)
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

    /**
     * Exchange JWT token
     *
     * @return token
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_NOT_FOUND
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#PASSWORD_INCORRECT
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#USER_DISABLED
     */
    @PostMapping("/token/retrieve")
    Result<String> retrieveToken(@Validated @RequestBody LoginVo vo);

    /**
     * Exchange JWT token
     *
     * @see com.curtisnewbie.service.auth.remote.consts.AuthServiceError#TOKEN_EXPIRED
     */
    @GetMapping("/token/{token}")
    Result<String> exchangeToken(@RequestParam("token") @NotEmpty String token);
}
