package com.curtisnewbie.service.auth.job;

import com.curtisnewbie.common.util.DateUtils;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.module.task.scheduling.AbstractJob;
import com.curtisnewbie.module.task.vo.TaskVo;
import com.curtisnewbie.service.auth.local.api.LocalOperateLogService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * Job that moves old operate_log records to operate_log_history
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Component
public class MoveOperateLogHistoryJob extends AbstractJob {

    @Autowired
    private LocalOperateLogService operateLogService;

    @Override
    protected void executeInternal(TaskVo task) throws JobExecutionException {
        final Date oneMonthBefore = DateUtils.dateOf(LocalDateTime.now().minusWeeks(1));

        log.info("Finding operate_log records before '{}'", oneMonthBefore);

        // records are moved, no need to change the page number
        PagingVo paging = new PagingVo().ofLimit(50).ofPage(1);

        PageInfo<Integer> ids = operateLogService.findIdsBeforeDateByPage(paging, oneMonthBefore);

        while (!ids.getList().isEmpty()) {

            log.info("Found {} operate_log records before '{}', moving them to operate_log_history",
                    ids.getList().size(), oneMonthBefore);

            operateLogService.moveRecordsToHistory(ids.getList());

            ids = operateLogService.findIdsBeforeDateByPage(paging, oneMonthBefore);
        }
    }
}
