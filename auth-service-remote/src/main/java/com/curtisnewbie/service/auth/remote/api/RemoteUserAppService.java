package com.curtisnewbie.service.auth.remote.api;

import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.UpdateUserAppReqCmd;
import com.curtisnewbie.service.auth.remote.vo.UserRequestAppApprovalCmd;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Remote service for UserApp
 *
 * @author yongjie.zhuang
 */
@Validated
public interface RemoteUserAppService {

    /**
     * Fetch the apps that the user is allowed to use
     */
    List<AppBriefVo> getAppsPermittedForUser(int userId);

    /** Update user's app */
    void updateUserApp(@NotNull UpdateUserAppReqCmd vo);

    /** Request approval to use the app */
    void requestAppUseApproval(@NotNull UserRequestAppApprovalCmd cmd);

    /**
     * Check if the user can use the specified application
     *
     * @param userId  user's id
     * @param appName application's name
     * @return true if it's allowed else false
     */
    boolean isUserAllowedToUseApp(int userId, @NotEmpty String appName);
}
