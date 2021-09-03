package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.service.auth.dao.UserEntity;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingStatus;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingType;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.exception.ExceededMaxAdminCountException;
import com.curtisnewbie.service.auth.remote.exception.UserRegisteredException;
import com.curtisnewbie.service.auth.remote.exception.UsernameNotFoundException;
import com.curtisnewbie.service.auth.remote.vo.EventHandlingVo;
import com.curtisnewbie.service.auth.remote.vo.RegisterUserVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yongjie.zhuang
 */
@Rollback
@Transactional
@SpringBootTest
public class TestLocalEventHandlingService {

    private static final String USERNAME = "eventhandling-username";
    private static final String PASSWORD = "eventhandling-password";
    private static final UserRole ROLE = UserRole.USER;
    private static final String CREATE_BY = "eventhandling-testcase";

    @Autowired
    private LocalEventHandlingService localEventHandlingService;

    @Autowired
    private LocalUserService userService;

    @Test
    public void shouldCreateEventHandlingRecord() {
        Assertions.assertDoesNotThrow(() -> {
            localEventHandlingService.createEvent(getEventHandlingVo());
        });
    }

    private EventHandlingVo getEventHandlingVo() throws UsernameNotFoundException, UserRegisteredException, ExceededMaxAdminCountException {
        registerTestUser();
        UserEntity ue = userService.loadUserByUsername(USERNAME);
        Assertions.assertNotNull(ue);
        final int userId = ue.getId();

        return EventHandlingVo.builder()
                .body(String.valueOf(userId))
                .status(EventHandlingStatus.TO_BE_HANDLED.getValue())
                .type(EventHandlingType.REGISTRATION_EVENT.getValue())
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
