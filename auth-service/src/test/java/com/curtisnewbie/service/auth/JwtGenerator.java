package com.curtisnewbie.service.auth;

import com.curtisnewbie.module.jwt.domain.api.JwtBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@SpringBootTest
public class JwtGenerator {

    @Autowired
    private JwtBuilder jwtBuilder;

    @Test
    public void generate() {
        Map<String, String> claims = new HashMap<>();
        claims.put("id", "-1");
        claims.put("username", "system");
        claims.put("role", "admin");

        log.info("token: {}", jwtBuilder.encode(claims, LocalDateTime.now().plusMinutes(20)));
    }

}
