package com.curtisnewbie.service.auth.infrastructure.job;

import com.curtisnewbie.module.task.annotation.JobDeclaration;
import com.curtisnewbie.module.task.scheduling.AbstractJob;
import com.curtisnewbie.module.task.vo.TaskVo;
import com.curtisnewbie.service.auth.local.api.LocalOperateLogService;
import com.curtisnewbie.service.auth.local.vo.cmd.MoveOperateLogToHistoryCmd;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * <p>
 * Job that moves old operate_log records to operate_log_history
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Component
@JobDeclaration(name = "Job that moves operate logs to history table", cron = "0 0 0/6 ? * *")
public class MoveOperateLogHistoryJob extends AbstractJob {

    @Autowired
    private LocalOperateLogService operateLogService;

    @Override
    protected void executeInternal(TaskVo task) throws JobExecutionException {
        final LocalDateTime before = LocalDateTime.now().minusMonths(1);
        log.info("Finding and moving operate_log records before '{}'", before);
        operateLogService.moveRecordsToHistory(
                MoveOperateLogToHistoryCmd.builder()
                        .batchSize(200)
                        .before(before)
                        .maxCount(10000)
                        .build()
        );
    }
}
