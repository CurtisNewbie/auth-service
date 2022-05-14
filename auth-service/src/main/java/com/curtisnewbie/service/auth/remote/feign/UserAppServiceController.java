package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.local.api.LocalUserAppService;
import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yongjie.zhuang
 */
@RequestMapping(value = UserAppServiceFeign.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class UserAppServiceController implements UserAppServiceFeign {

    @Autowired
    private LocalUserAppService localUserAppService;

    @Override
    public Result<List<AppBriefVo>> getAppsPermittedForUser(int userId) {
        return Result.of(localUserAppService.getAppsPermittedForUser(userId));
    }

    @Override
    public Result<Boolean> isUserAllowedToUseApp(int userId, String appName) {
        return Result.of(localUserAppService.isUserAllowedToUseApp(userId, appName));
    }
}
