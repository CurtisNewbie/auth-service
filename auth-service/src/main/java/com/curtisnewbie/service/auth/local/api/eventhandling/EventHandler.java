package com.curtisnewbie.service.auth.local.api.eventhandling;

import com.curtisnewbie.service.auth.vo.HandleEventInfoVo;

/**
 * Handler of events
 *
 * @author yongjie.zhuang
 */
public interface EventHandler {

    String EVENT_HANDLER_EXCHANGE = "auth.event-handler.exg";

    String EVENT_HANDLER_QUEUE = "auth.event-handler.queue";

    /**
     * Handle event
     */
    void handle(HandleEventInfoVo eh);
}
