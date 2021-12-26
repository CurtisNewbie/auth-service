package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.service.auth.remote.api.RemoteUserAppService;
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
public interface LocalUserAppService extends RemoteUserAppService {

    /**
     * Add userApp record
     */
    void addUserApp(int userId, int appId, @NotEmpty String createdBy);
}
