package com.curtisnewbie.service.auth.config;

import com.curtisnewbie.service.auth.messaging.helper.OperateLogAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yongj.zhuang
 */
@Configuration
public class OperateLogAdviceConfig {

    @Bean
    public OperateLogAdvice operateLogAdvice() {
        return new OperateLogAdvice();
    }
}
