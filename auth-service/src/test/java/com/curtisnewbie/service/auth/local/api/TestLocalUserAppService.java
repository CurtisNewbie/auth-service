package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.AuthServiceApplication;
import com.curtisnewbie.service.auth.dao.TestMapperConfig;
import com.curtisnewbie.service.auth.remote.vo.UserRequestAppApprovalCmd;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test {@link LocalUserAppService}
 *
 * @author yongjie.zhuang
 */
@Rollback
@Transactional
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AuthServiceApplication.class, TestMapperConfig.class})
public class TestLocalUserAppService {

    @MockBean
    LocalEventHandlingService eventHandlingServiceMock;
    @Autowired
    LocalUserAppService localUserAppService;

    @Test
    public void shouldRequestUserAppApproval() {
        // mock the createEvent method, return random id
        Mockito.when(eventHandlingServiceMock.createEvent(Mockito.any())).thenAnswer(invok -> {
            log.info("Mocked LocalEventHandlingService.createEvent(...) method");
            return 1;
        });

        Assertions.assertDoesNotThrow(() -> {
            localUserAppService.requestAppUseApproval(UserRequestAppApprovalCmd.builder()
                    .userId(123123)
                    .appId(123123)
                    .build());
        });

    }
}
