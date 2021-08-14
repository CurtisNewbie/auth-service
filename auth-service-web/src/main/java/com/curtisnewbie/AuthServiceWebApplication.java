package com.curtisnewbie;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.io.IOException;

@EnableRedisHttpSession
@PropertySources({
        @PropertySource("classpath:dubbo.properties"),
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:common.properties"),
})
@EnableDubbo
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication
public class AuthServiceWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceWebApplication.class, args);
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
