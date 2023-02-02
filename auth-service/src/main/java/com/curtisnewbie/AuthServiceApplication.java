package com.curtisnewbie;

import com.curtisnewbie.common.advice.EnableRoleControl;
import com.curtisnewbie.common.dao.*;
import com.curtisnewbie.goauth.client.*;
import com.curtisnewbie.module.messaging.listener.EnableMsgListener;
import com.curtisnewbie.service.auth.messaging.helper.EnableOperateLog;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.*;

@EnableFeignClients
@EnableGoauthPathReport
@EnableRoleControl
@EnableOperateLog
@EnableMsgListener
@MapperScan("com.curtisnewbie.service.auth.infrastructure.repository.mapper")
@EnableDiscoveryClient
@SpringBootApplication
@EnableMBTraceInterceptor
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
