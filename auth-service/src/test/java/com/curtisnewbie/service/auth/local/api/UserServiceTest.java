package com.curtisnewbie.service.auth.local.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void should_fetch_username_by_user_nos() {
        var userNos = Arrays.asList("UE202205142310076187414", "UE202205142310074386952");
        final Map<String, String> userNoMap = userService.fetchUsernameByUserNos(userNos);
        log.info("UserNoMap: {}", userNoMap);

        Assertions.assertNotNull(userNoMap);
        Assertions.assertFalse(userNoMap.isEmpty());
        userNos.forEach(un -> Assertions.assertTrue(userNoMap.containsKey(un) && StringUtils.hasText(userNoMap.get(un))));
    }

}