package com.curtisnewbie.auth.config;

import com.curtisnewbie.common.vo.Result;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Generic fallback handler for sentinel
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
public class SentinelFallbackConfig {

    public static Result<Void> serviceNotAvailable(Exception e) {
        log.error("Exception occurred, using generic fallback methods", e);
        return Result.error("Server is busy, please try again later");
    }
}
