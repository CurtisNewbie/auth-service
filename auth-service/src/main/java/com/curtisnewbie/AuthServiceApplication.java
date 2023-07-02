package com.curtisnewbie;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.*;
import com.baomidou.mybatisplus.extension.plugins.inner.*;
import com.curtisnewbie.common.dao.*;
import com.curtisnewbie.common.util.*;
import com.curtisnewbie.goauth.client.*;
import com.curtisnewbie.module.messaging.listener.EnableMsgListener;
import com.curtisnewbie.service.auth.messaging.helper.EnableOperateLog;
import com.fasterxml.jackson.databind.*;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.*;
import org.springframework.context.annotation.*;

@EnableFeignClients
@EnableGoauthPathReport
@EnableOperateLog
@EnableMsgListener
@MapperScan("com.curtisnewbie.service.auth.infrastructure.repository.mapper")
@EnableDiscoveryClient
@EnableMBTraceInterceptor
@EnableAspectJAutoProxy
@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper om = JsonUtils.constructsEpochJsonMapper();
        return om;
    }
}
