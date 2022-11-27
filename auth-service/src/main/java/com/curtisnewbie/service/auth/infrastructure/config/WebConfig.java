package com.curtisnewbie.service.auth.infrastructure.config;

import com.curtisnewbie.common.formatters.DateEpochFormatter;
import com.curtisnewbie.common.formatters.LocalDateTimeEpochFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration of Web MVC
 *
 * @author yongjie.zhuang
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateEpochFormatter());
        registry.addFormatter(new LocalDateTimeEpochFormatter());
    }

}
