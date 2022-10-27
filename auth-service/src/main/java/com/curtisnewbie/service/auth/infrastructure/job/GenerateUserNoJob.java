package com.curtisnewbie.service.auth.infrastructure.job;

import com.curtisnewbie.common.util.LDTTimer;
import com.curtisnewbie.module.task.annotation.JobDeclaration;
import com.curtisnewbie.module.task.scheduling.AbstractJob;
import com.curtisnewbie.module.task.vo.TaskVo;
import com.curtisnewbie.service.auth.local.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * Job that generate user_no (for backward compatible)
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Component
@JobDeclaration(name = "Job that generates the missing user no", cron = "0 0 0 ? * *")
public class GenerateUserNoJob extends AbstractJob {

    @Autowired
    private UserService userService;

    @Override
    protected void executeInternal(TaskVo task) throws JobExecutionException {
        final LDTTimer timer = LDTTimer.startTimer();
        final List<Integer> ids = userService.listEmptyUserNoId();
        ids.forEach(id -> userService.generateUserNoIfEmpty(id));
        if (task != null) task.setLastRunResult(String.format("Generated userNo for %s records", ids.size()));
        log.info("GenerateUserNoJob finished, processed records: {}, time: {}", ids.size(), timer.stop().printDuration());
    }
}
