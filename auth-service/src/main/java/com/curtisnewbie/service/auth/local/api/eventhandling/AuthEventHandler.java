package com.curtisnewbie.service.auth.local.api.eventhandling;

import com.curtisnewbie.service.auth.remote.consts.EventHandlingType;
import com.curtisnewbie.service.auth.local.vo.cmd.HandleEventInfoVo;

/**
 * Handler of events
 * <p>
 * Bean name must be {@link #BEAN_NAME_PREFIX} + {@link EventHandlingType#name()}
 * </p>
 *
 * @author yongjie.zhuang
 */
public interface AuthEventHandler {

    String BEAN_NAME_PREFIX = "auth-event-handler-";

    /**
     * Handle event
     */
    void handle(HandleEventInfoVo eh);
}
