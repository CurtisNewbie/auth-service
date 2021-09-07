package com.curtisnewbie.auth.config;

import com.curtisnewbie.common.util.JsonUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

/**
 * Config for {@link com.fasterxml.jackson.databind.ObjectMapper}
 *
 * @author yongjie.zhuang
 */
@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return JsonUtils.constructsJsonMapper();
    }
}
