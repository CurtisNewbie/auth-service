package com.curtisnewbie;

import org.mybatis.spring.annotation.MapperScan;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.io.IOException;

@MapperScan("com.curtisnewbie.service.auth.infrastructure.repository.mapper")
@EnableDiscoveryClient
@SpringBootApplication
@EnableRedisHttpSession(redisNamespace = "auth-service:session") // todo remove this
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
