package com.curtisnewbie.service.auth.infrastructure.mq.listeners;

import com.curtisnewbie.module.messaging.listener.MsgListener;
import com.curtisnewbie.service.auth.local.api.LocalAccessLogService;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Listener that saves access_log
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Component
public class AccessLogSaveListener {

    @Autowired
    private LocalAccessLogService localAccessLogService;

    @MsgListener(queue = "auth.access-log.queue", exchange = "auth.access-log.exg", routingKey = "auth.access-log.save")
    public void onMessage(AccessLogInfoVo vo) {
        if (vo == null) {
            log.error("Received null messages, unable to save access_log");
            return;
        }

        localAccessLogService.save(vo);
        log.debug("Save access_log: '{}'", vo);
    }
}
