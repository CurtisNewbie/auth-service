package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

/**
 * Test {@link LocalOperateLogService}
 *
 * @author yongjie.zhuang
 */
@SpringBootTest
@Rollback
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
        v.setOperateTime(new Date());
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
