package com.curtisnewbie.service.auth.infrastructure.job;

import com.curtisnewbie.common.util.Paginator;
import com.curtisnewbie.module.task.scheduling.AbstractJob;
import com.curtisnewbie.module.task.vo.TaskVo;
import com.curtisnewbie.service.auth.local.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Job that generate user_no (for backward compatible)
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Component
public class GenerateUserNoJob extends AbstractJob {

    @Autowired
    private UserService userService;

    @Override
    protected void executeInternal(TaskVo task) throws JobExecutionException {
        Paginator<Integer /* id */> paginator = new Paginator<Integer>()
                .pageSize(50)
                .isTimed(true)
                .nextPageSupplier(p -> userService.listEmptyUserNoId(p.getOffset(), p.getLimit()));

        paginator.loopEachTilEnd(id -> {
            userService.generateUserNoIfEmpty(id);
        });

        log.info("GenerateUserNoJob finished, processed records: {}, time: {}", paginator.getCount(), paginator.getTimer().printDuration());
    }
}
