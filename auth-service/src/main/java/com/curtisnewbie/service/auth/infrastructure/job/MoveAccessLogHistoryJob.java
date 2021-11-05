package com.curtisnewbie.service.auth.infrastructure.job;

import com.curtisnewbie.module.task.scheduling.AbstractJob;
import com.curtisnewbie.module.task.vo.TaskVo;
import com.curtisnewbie.service.auth.local.api.LocalAccessLogService;
import com.curtisnewbie.service.auth.vo.MoveAccessLogToHistoryCmd;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * <p>
 * Job that moves old access_log records to access_log_history
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Component
public class MoveAccessLogHistoryJob extends AbstractJob {

    @Autowired
    private LocalAccessLogService accessLogService;

    @Override
    protected void executeInternal(TaskVo task) throws JobExecutionException {
        final LocalDateTime oneWeekBefore = LocalDateTime.now().minusWeeks(1);

        log.info("Finding and moving access_log records before '{}'", oneWeekBefore);
        accessLogService.moveRecordsToHistory(
                MoveAccessLogToHistoryCmd.builder()
                        .batchSize(200)
                        .before(oneWeekBefore)
                        .maxCount(10000)
                        .build()
        );

    }
}
