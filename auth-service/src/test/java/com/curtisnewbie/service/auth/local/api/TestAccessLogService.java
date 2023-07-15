package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.AuthServiceApplication;
import com.curtisnewbie.common.vo.PageableList;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.dao.AccessLogTestMapper;
import com.curtisnewbie.service.auth.dao.TestMapperConfig;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Test {@link AccessLogService}
 *
 * @author yongjie.zhuang
 */
@SpringBootTest(classes = {AuthServiceApplication.class, TestMapperConfig.class})
public class TestAccessLogService {

    private static final String TEST_IP = "xxx.xxx.xxx.xxx";

    @Autowired
    AccessLogService localAccessLogService;

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
        PageableList<AccessLogInfoVo> pi = localAccessLogService.findAccessLogInfoByPage(pagingVo);
        Assertions.assertNotNull(pi);
        Assertions.assertFalse(pi.getPayload().isEmpty());
    }

    private void saveTestAccessLogInfo() {
        AccessLogInfoVo log = new AccessLogInfoVo();
        log.setAccessTime(LocalDateTime.now());
        log.setIpAddress(TEST_IP);
        log.setUserId(1);
        log.setUsername("yongjie.zhuang");
        localAccessLogService.save(log);
    }

}
