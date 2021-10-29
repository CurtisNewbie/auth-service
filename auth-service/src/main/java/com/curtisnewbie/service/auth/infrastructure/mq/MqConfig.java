package com.curtisnewbie.service.auth.infrastructure.mq;

import com.curtisnewbie.module.messaging.config.SimpleConnectionFactoryBeanFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


/**
 * Config for mq
 *
 * @author yongjie.zhuang
 */
@EnableRabbit
@Configuration
public class MqConfig {

    @Autowired
    private Environment environment;

    @Bean
    public ConnectionFactory connectionFactory() {
        return SimpleConnectionFactoryBeanFactory.createByProperties(environment);
    }
}
