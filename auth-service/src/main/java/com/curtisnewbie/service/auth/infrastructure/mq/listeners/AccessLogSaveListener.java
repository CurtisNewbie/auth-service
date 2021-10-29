package com.curtisnewbie.service.auth.infrastructure.mq.listeners;

import com.curtisnewbie.service.auth.local.api.LocalAccessLogService;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
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

    @RabbitListener(
            bindings = @QueueBinding(
                    exchange = @Exchange(name = "auth.access-log.exg"),
                    value = @Queue(name = "auth.access-log.queue", durable = "true"),
                    key = "auth.access-log.save"),
            ackMode = "AUTO"
    )
    public void onMessage(AccessLogInfoVo vo) {
        if (vo == null) {
            log.error("Received null messages, unable to save access_log");
            return;
        }
        log.info("Save access_log: '{}'", vo);
        localAccessLogService.save(vo);

    }
}
