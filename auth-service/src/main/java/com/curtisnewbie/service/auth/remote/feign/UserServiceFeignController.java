package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.local.api.LocalUserService;
import com.curtisnewbie.service.auth.remote.vo.FetchUsernameByIdReq;
import com.curtisnewbie.service.auth.remote.vo.FetchUsernameByIdResp;
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
    private LocalUserService localUserService;

    @Override
    public Result<String> findUsernameById(int id) {
        return Result.of(localUserService.findUsernameById(id));
    }

    @Override
    public Result<Integer> findIdByUsername(String username) {
        return Result.of(localUserService.findIdByUsername(username));
    }

    @Override
    public Result<FetchUsernameByIdResp> fetchUsernameById(FetchUsernameByIdReq req) {
        return Result.of(FetchUsernameByIdResp.builder()
                .idToUsername(localUserService.fetchUsernameById(req.getUserIds()))
                .build());
    }

}
