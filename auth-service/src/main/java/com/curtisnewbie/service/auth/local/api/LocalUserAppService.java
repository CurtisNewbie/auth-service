package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.UpdateUserAppReqCmd;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * Local service for user and applications
 * </p>
 *
 * @author yongjie.zhuang
 */
@Validated
public interface LocalUserAppService {

    /**
     * Add userApp record
     */
    void addUserApp(int userId, int appId, @NotEmpty String createdBy);

    /**
     * Fetch the apps that the user is allowed to use
     */
    List<AppBriefVo> getAppsPermittedForUser(int userId);

    /** Update user's app */
    void updateUserApp(@NotNull UpdateUserAppReqCmd vo);

    /**
     * Check if the user can use the specified application
     *
     * @param userId  user's id
     * @param appName application's name
     * @return true if it's allowed else false
     */
    boolean isUserAllowedToUseApp(int userId, @NotEmpty String appName);
}
