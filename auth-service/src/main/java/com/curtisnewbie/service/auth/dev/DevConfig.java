package com.curtisnewbie.service.auth.dev;

import com.curtisnewbie.common.advice.ControllerConsoleLogAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuration only used for dev
 *
 * @author yongj.zhuang
 */
@Profile("dev")
@Configuration
public class DevConfig {

    @Bean
    public ControllerConsoleLogAdvice controllerConsoleLogAdvice() {
        return new ControllerConsoleLogAdvice();
    }
}
