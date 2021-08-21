package com.curtisnewbie.service.auth.mq.listeners;

import com.curtisnewbie.service.auth.local.api.LocalOperateLogService;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Listener that saves operate_log
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Component
public class OperateLogSaveListener {

    @Autowired
    private LocalOperateLogService operateLogService;

    @RabbitListener(
            bindings = @QueueBinding(
                    exchange = @Exchange(name = "auth.operate-log.exg"),
                    value = @Queue(name = "auth.operate-log.queue", durable = "true"),
                    key = "auth.operate-log.save"),
            ackMode = "AUTO"
    )
    public void onMessage(OperateLogVo vo) {
        if (vo == null) {
            log.error("Received null messages, unable to save operate_log");
            return;
        }

        log.info("Save operate_log: '{}'", vo);
        operateLogService.saveOperateLogInfo(vo);
    }
}
