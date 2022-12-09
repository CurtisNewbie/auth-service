package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.local.api.UserService;
import com.curtisnewbie.service.auth.remote.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yongjie.zhuang
 */
@RequestMapping(value = UserServiceFeign.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class UserServiceFeignController implements UserServiceFeign {

    @Autowired
    private UserService userService;

    @Override
    public Result<String> findUsernameById(int id) {
        return Result.of(userService.findUsernameById(id));
    }

    @Override
    public Result<UserInfoVo> findUser(FindUserReq req) {
        return Result.of(userService.findUser(req));
    }

    @Override
    public Result<Integer> findIdByUsername(String username) {
        return Result.of(userService.findIdByUsername(username));
    }

    @Override
    public Result<FetchUsernameByIdResp> fetchUsernameById(FetchUsernameByIdReq req) {
        return Result.of(FetchUsernameByIdResp.builder()
                .idToUsername(userService.fetchUsernameById(req.getUserIds()))
                .build());
    }

    @Override
    public Result<FetchUsernameByUserNosResp> fetchUsernameByUserNos(FetchUsernameByUserNosReq req) {
        return Result.of(FetchUsernameByUserNosResp.builder()
                .userNoToUsername(userService.fetchUsernameByUserNos(req.getUserNos()))
                .build());
    }

    @Override
    public Result<UserInfoVo> fetchUserInfo(int userId) {
        return Result.of(userService.getUserInfo(userId));
    }

    @Override
    public Result<UserInfoVo> fetchUserInfo(String userNo) {
        return Result.of(userService.getUserInfo(userNo));
    }

}
