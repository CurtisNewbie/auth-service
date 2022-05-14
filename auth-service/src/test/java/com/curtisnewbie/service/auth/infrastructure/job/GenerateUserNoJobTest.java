package com.curtisnewbie.service.auth.infrastructure.job;

import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author yongj.zhuang
 */
@SpringBootTest
public class GenerateUserNoJobTest {

    @Autowired
    private GenerateUserNoJob job;

    @Test
    public void should_generate_user_no() throws JobExecutionException {
        job.executeInternal(null);
    }
}
