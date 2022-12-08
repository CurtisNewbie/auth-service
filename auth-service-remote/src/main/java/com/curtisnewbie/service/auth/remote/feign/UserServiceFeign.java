package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.remote.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;

/**
 * Feign for User
 *
 * @author yongjie.zhuang
 */
@FeignClient(value = FeignConst.SERVICE_NAME, path = UserServiceFeign.PATH)
public interface UserServiceFeign {

    String PATH = "/remote/user";

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
     * Fetch username by ids
     *
     * @return user_id -> username map
     */
    @PostMapping(value = "/username")
    Result<FetchUsernameByIdResp> fetchUsernameById(@Validated @RequestBody FetchUsernameByIdReq req);

    /**
     * Fetch username by userNos
     *
     * @return user_no -> username map
     */
    @PostMapping(value = "/userno/username")
    Result<FetchUsernameByUserNosResp> fetchUsernameByUserNos(@Validated @RequestBody FetchUsernameByUserNosReq req);

    /**
     * fetch user info by id
     */
    @GetMapping("/info-by-id")
    Result<UserInfoVo> fetchUserInfo(@NotBlank @RequestParam("userId") int userId);

    /**
     * fetch user info by userNo
     */
    @GetMapping("/info-by-userno")
    Result<UserInfoVo> fetchUserInfo(@NotBlank @RequestParam("userNo") String userNo);
}
