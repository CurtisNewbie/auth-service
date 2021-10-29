package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.AuthServiceApplication;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.dao.AccessLogTestMapper;
import com.curtisnewbie.service.auth.dao.TestMapperConfig;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Test {@link LocalAccessLogService}
 *
 * @author yongjie.zhuang
 */
@SpringBootTest(classes = {AuthServiceApplication.class, TestMapperConfig.class})
public class TestLocalAccessLogService {

    private static final String TEST_IP = "xxx.xxx.xxx.xxx";

    @Autowired
    LocalAccessLogService localAccessLogService;

    @Autowired
    AccessLogTestMapper testMapper;

    @Test
    @Transactional
    @Rollback
    public void shouldSaveAccessLogInfo() {
        saveTestAccessLogInfo();
        Assertions.assertNotNull(testMapper.findIdByIp(TEST_IP));
    }

    @Test
    @Transactional
    @Rollback
    public void shouldFindAccessLogInfoByPage() {
        saveTestAccessLogInfo();

        PagingVo pagingVo = new PagingVo();
        pagingVo.setLimit(10);
        pagingVo.setPage(1);
        PageInfo<AccessLogInfoVo> pi = localAccessLogService.findAccessLogInfoByPage(pagingVo);
        Assertions.assertNotNull(pi);
        Assertions.assertFalse(pi.getList().isEmpty());
    }

    private void saveTestAccessLogInfo() {
        AccessLogInfoVo log = new AccessLogInfoVo();
        log.setAccessTime(new Date());
        log.setIpAddress(TEST_IP);
        log.setUserId(1);
        log.setUsername("yongjie.zhuang");
        localAccessLogService.save(log);
    }

}
