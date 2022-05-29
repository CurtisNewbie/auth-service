package com.curtisnewbie.service.auth.messaging.helper;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enable Operation Log
 *
 * @author yongj.zhuang
 * @see LogOperation
 * @see OperateLogAdvice
 */
@Documented
@Import(OperateLogAdvice.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableOperateLog {
}
