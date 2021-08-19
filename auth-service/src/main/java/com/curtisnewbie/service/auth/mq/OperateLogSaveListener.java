package com.curtisnewbie.service.auth.mq;

import com.curtisnewbie.module.messaging.listener.AbstractMessageListener;
import com.curtisnewbie.service.auth.local.api.LocalOperateLogService;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Listener that saves operate_log
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Component
public class OperateLogSaveListener extends AbstractMessageListener<OperateLogVo> {

    @Autowired
    private LocalOperateLogService operateLogService;

    @Override
    protected void onMessage(OperateLogVo v, Message message) {
        if (v == null) {
            log.error("Received null messages, unable to save operate_log");
        }

        log.debug("Log operate_log message: '{}'", v);
        operateLogService.saveOperateLogInfo(v);
    }

    @Override
    protected Class<OperateLogVo> messageType() {
        return OperateLogVo.class;
    }
}
