package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.AuthServiceApplication;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.module.messaging.service.MessagingParam;
import com.curtisnewbie.module.messaging.service.MessagingService;
import com.curtisnewbie.service.auth.dao.TestMapperConfig;
import com.curtisnewbie.service.auth.dao.User;
import com.curtisnewbie.service.auth.infrastructure.mq.listeners.DelegatingAuthEventListener;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingResult;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingStatus;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingType;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.exception.ExceededMaxAdminCountException;
import com.curtisnewbie.service.auth.remote.exception.UserRegisteredException;
import com.curtisnewbie.service.auth.remote.exception.UsernameNotFoundException;
import com.curtisnewbie.service.auth.remote.vo.*;
import com.curtisnewbie.service.auth.local.vo.cmd.UpdateHandleStatusCmd;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author yongjie.zhuang
 */
@Rollback
@Transactional
@SpringBootTest(classes = {AuthServiceApplication.class, TestMapperConfig.class})
public class TestLocalEventHandlingService {

    private static final String USERNAME = "eventhandling-username";
    private static final String PASSWORD = "eventhandling-password";
    private static final UserRole ROLE = UserRole.USER;
    private static final String CREATE_BY = "eventhandling-testcase";

    @Autowired
    private LocalEventHandlingService localEventHandlingService;

    @Autowired
    private LocalUserService userService;

    @MockBean
    private MessagingService mockedMessagingService;

    @Captor
    ArgumentCaptor<MessagingParam> captor;

    @Test
    public void shouldCreateEventHandlingRecord() {
        Assertions.assertDoesNotThrow(() -> {
            createEvent();
        });
    }

    @Test
    public void shouldUpdateHandleStatus() {
        Assertions.assertDoesNotThrow(() -> {

            final int id = createEvent();

            Assertions.assertTrue(
                    localEventHandlingService.updateHandleStatus(UpdateHandleStatusCmd.builder()
                            .id(id)
                            .prevStatus(EventHandlingStatus.TO_BE_HANDLED)
                            .currStatus(EventHandlingStatus.HANDLED)
                            .handlerId(1) // random user_id
                            .handleTime(LocalDateTime.now())
                            .build()));
        });
    }

    @Test
    public void shouldFindEventHandlingListByPage() {
        Assertions.assertDoesNotThrow(() -> {
            FindEventHandlingByPageReqVo param = new FindEventHandlingByPageReqVo();
            param.setPagingVo(new PagingVo());
            localEventHandlingService.findEventHandlingByPage(param);
        });
    }

    @Test
    public void shouldHandleRegistrationEvent() throws UsernameNotFoundException, UserRegisteredException, ExceededMaxAdminCountException {

        int id = createEvent();
        localEventHandlingService.handleEvent(HandleEventReqVo.builder()
                .id(id)
                .handlerId(1) // random user_id
                .result(EventHandlingResult.ACCEPT)
                .build());

        // capture the argument
        Mockito.verify(mockedMessagingService).send(captor.capture());

        // we are testing using registration type event
        // verify the captured argument
        MessagingParam captured = captor.getValue();
        Assertions.assertNotNull(captured);
        Assertions.assertEquals(captured.getExchange(), DelegatingAuthEventListener.EVENT_HANDLER_EXCHANGE);
        Assertions.assertEquals(captured.getRoutingKey(), DelegatingAuthEventListener.ROUTING_KEY);
    }

    /**
     * @return id of event_handling
     */
    private int createEvent() throws UsernameNotFoundException, UserRegisteredException, ExceededMaxAdminCountException {
        return localEventHandlingService.createEvent(getCreateEventHandlingCmd());
    }

    private CreateEventHandlingCmd getCreateEventHandlingCmd() throws UsernameNotFoundException, UserRegisteredException, ExceededMaxAdminCountException {
        registerTestUser();
        User ue = userService.loadUserByUsername(USERNAME);
        Assertions.assertNotNull(ue);
        final int userId = ue.getId();

        return CreateEventHandlingCmd.builder()
                .body(String.valueOf(userId))
                .type(EventHandlingType.REGISTRATION_EVENT)
                .build();
    }

    private void registerTestUser() throws UserRegisteredException, ExceededMaxAdminCountException {
        userService.register(getRegisterUser());
    }

    private RegisterUserVo getRegisterUser() {
        RegisterUserVo v = new RegisterUserVo();
        v.setUsername(USERNAME);
        v.setPassword(PASSWORD);
        v.setRole(ROLE);
        v.setCreateBy(CREATE_BY);
        return v;
    }
}
