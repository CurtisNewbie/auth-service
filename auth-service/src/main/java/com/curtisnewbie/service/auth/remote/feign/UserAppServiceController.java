package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.local.api.LocalUserAppService;
import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.UpdateUserAppReqCmd;
import com.curtisnewbie.service.auth.remote.vo.UserRequestAppApprovalCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yongjie.zhuang
 */
@RestController
public class UserAppServiceController implements UserAppServiceFeign {

    @Autowired
    private LocalUserAppService localUserAppService;

    @Override
    public Result<List<AppBriefVo>> getAppsPermittedForUser(int userId) {
        return Result.of(localUserAppService.getAppsPermittedForUser(userId));
    }

    @Override
    public Result<Void> updateUserApp(UpdateUserAppReqCmd vo) {
        localUserAppService.updateUserApp(vo);
        return Result.ok();
    }

    @Override
    public Result<Void> requestAppUseApproval(UserRequestAppApprovalCmd cmd) {
        localUserAppService.requestAppUseApproval(cmd);
        return Result.ok();
    }

    @Override
    public Result<Boolean> isUserAllowedToUseApp(int userId, String appName) {
        return Result.of(localUserAppService.isUserAllowedToUseApp(userId, appName));
    }
}
