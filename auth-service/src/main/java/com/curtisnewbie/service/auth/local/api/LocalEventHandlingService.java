package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.service.auth.remote.api.RemoteEventHandlingService;
import com.curtisnewbie.service.auth.vo.UpdateHandleStatusReqVo;

import javax.validation.constraints.NotNull;

/**
 * Local service for event_handling
 *
 * @author yongjie.zhuang
 */
public interface LocalEventHandlingService extends RemoteEventHandlingService {

    /**
     * Update handling result
     *
     * @return record updated
     */
    boolean updateHandleStatus(@NotNull UpdateHandleStatusReqVo param);
}
