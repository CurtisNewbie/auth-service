package com.curtisnewbie.service.auth.job;

import com.curtisnewbie.common.util.DateUtils;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.local.api.LocalAccessLogService;
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
 * Job that cleanup access_log
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Component
public class MoveAccessLogHistoryJob implements Job {

    @Autowired
    private LocalAccessLogService accessLogService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        final Date oneMonthBefore = DateUtils.dateOf(LocalDateTime.now().minusMonths(1));

        log.info("Finding access_log records before '{}'", oneMonthBefore);

        // records are moved, no need to change the page number
        PagingVo paging = new PagingVo().ofLimit(50).ofPage(1);

        PageInfo<Integer> ids = accessLogService.findIdsBeforeDateByPage(paging, oneMonthBefore);

        while (!ids.getList().isEmpty()) {

            log.info("Found {} access_log records before '{}', moving them to access_log_history",
                    ids.getList().size(), oneMonthBefore);

            accessLogService.moveRecordsToHistory(ids.getList());

            ids = accessLogService.findIdsBeforeDateByPage(paging, oneMonthBefore);
        }
    }
}
