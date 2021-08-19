package com.curtisnewbie.service.auth.mq;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * Config for mq
 *
 * @author yongjie.zhuang
 */
@ImportResource("classpath:rabbitmq.xml")
@Configuration
public class MqConfig {
}
