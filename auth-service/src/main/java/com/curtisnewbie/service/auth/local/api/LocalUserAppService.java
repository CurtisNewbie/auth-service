package com.curtisnewbie.service.auth.local.api;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

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
     * Check if the user can use the specified application
     *
     * @param userId  user's id
     * @param appName application's name
     * @return true if it's allowed else false
     */
    boolean isUserAllowedToUseApp(int userId, @NotEmpty String appName);
}
