package com.curtisnewbie;

import com.curtisnewbie.common.advice.EnableRoleControl;
import com.curtisnewbie.module.messaging.listener.EnableMsgListener;
import com.curtisnewbie.service.auth.messaging.helper.EnableOperateLog;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableRoleControl
@EnableOperateLog
@EnableMsgListener
@MapperScan("com.curtisnewbie.service.auth.infrastructure.repository.mapper")
@EnableDiscoveryClient
@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

}
