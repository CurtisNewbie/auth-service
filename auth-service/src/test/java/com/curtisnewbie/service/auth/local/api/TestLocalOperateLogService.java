package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.AuthServiceApplication;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.dao.TestMapperConfig;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Test {@link LocalOperateLogService}
 *
 * @author yongjie.zhuang
 */
@Transactional
@Rollback
@SpringBootTest(classes = {AuthServiceApplication.class, TestMapperConfig.class})
public class TestLocalOperateLogService {

    @Autowired
    private LocalOperateLogService operateLogService;

    @Test
    public void shouldSaveOperateLogInfo() {
        OperateLogVo v = new OperateLogVo();
        v.setUserId(1);
        v.setUsername("yongj.zhuang");
        v.setOperateDesc("Add new user");
        v.setOperateName("New-User");
        v.setOperateTime(LocalDateTime.now());
        v.setOperateParam("{ \"username\" : \"sharon\" }");

        Assertions.assertDoesNotThrow(() -> {
            operateLogService.saveOperateLogInfo(v);
        });
    }

    @Test
    public void shouldFindOperateLogInfoInPages() {
        PagingVo pv = new PagingVo();
        pv.setPage(1);
        pv.setLimit(10);
        Assertions.assertDoesNotThrow(() -> {
            operateLogService.findOperateLogInfoInPages(pv);
        });
    }
}
