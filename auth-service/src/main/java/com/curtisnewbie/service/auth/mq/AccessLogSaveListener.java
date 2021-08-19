package com.curtisnewbie.service.auth.mq;

import com.curtisnewbie.module.messaging.listener.AbstractMessageListener;
import com.curtisnewbie.service.auth.local.api.LocalAccessLogService;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Listener that saves access_log
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Component
public class AccessLogSaveListener extends AbstractMessageListener<AccessLogInfoVo> {

    @Autowired
    private LocalAccessLogService localAccessLogService;

    @Override
    protected void onMessage(AccessLogInfoVo v, Message message) {
        if (v == null) {
            log.error("Received null messages, unable to save access_log");
        }
        log.debug("Log access_log message: '{}'", v);
        localAccessLogService.save(v);

    }

    @Override
    protected Class<AccessLogInfoVo> messageType() {
        return AccessLogInfoVo.class;
    }
}
