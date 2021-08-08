package com.curtisnewbie;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@MapperScan("com.curtisnewbie.service.auth.dao")
@PropertySources({
        @PropertySource(value = "classpath:dubbo.properties"),
        @PropertySource(value = "classpath:application.properties"),
        @PropertySource(value = "classpath:common.properties")
})
@EnableDubbo
@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

}
