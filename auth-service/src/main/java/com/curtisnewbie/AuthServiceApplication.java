package com.curtisnewbie;

import com.curtisnewbie.common.advice.EnableRoleControl;
import com.curtisnewbie.module.messaging.listener.EnableMsgListener;
import com.curtisnewbie.service.auth.messaging.helper.EnableOperateLog;
import org.mybatis.spring.annotation.MapperScan;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

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

    @Value("${redisson-config}")
    private String redissonConfig;

    @Bean
    public RedissonClient redissonClient() throws IOException {
        Config config = Config.fromYAML(this.getClass().getClassLoader().getResourceAsStream(redissonConfig));
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

}
