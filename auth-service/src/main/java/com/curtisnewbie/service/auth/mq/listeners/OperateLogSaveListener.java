package com.curtisnewbie.service.auth.mq.listeners;

import com.curtisnewbie.service.auth.local.api.LocalOperateLogService;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import lombok.extern.slf4j.Slf4j;
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

    public void onMessage(OperateLogVo vo) {
        if (vo == null) {
            log.error("Received null messages, unable to save operate_log");
        }

        log.debug("Log operate_log message: '{}'", vo);
        operateLogService.saveOperateLogInfo(vo);
    }
}
