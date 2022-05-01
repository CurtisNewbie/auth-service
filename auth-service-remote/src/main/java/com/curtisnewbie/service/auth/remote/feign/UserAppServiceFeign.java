package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.UserRequestAppApprovalCmd;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author yongjie.zhuang
 */
@FeignClient(value = FeignConst.SERVICE_NAME, path = UserAppServiceFeign.PATH)
public interface UserAppServiceFeign {

    String PATH = "/remote/userapp";

    /**
     * Fetch the apps that the user is allowed to use
     */
    @GetMapping("/list")
    Result<List<AppBriefVo>> getAppsPermittedForUser(@RequestParam("userId") int userId);

    /** Request approval to use the app */
    @PostMapping("/request-approval")
    Result<Void> requestAppUseApproval(@RequestBody UserRequestAppApprovalCmd cmd);

    /**
     * Check if the user can use the specified application
     *
     * @param userId  user's id
     * @param appName application's name
     * @return true if it's allowed else false
     */
    @GetMapping("/check-permission")
    Result<Boolean> isUserAllowedToUseApp(@RequestParam("userId") int userId, @RequestParam("appName") String appName);
}
