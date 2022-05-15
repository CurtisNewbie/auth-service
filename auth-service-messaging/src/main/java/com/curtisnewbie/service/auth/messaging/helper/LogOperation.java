package com.curtisnewbie.service.auth.messaging.helper;

import java.lang.annotation.*;

/**
 * Annotation indicating that a method is a logged operation
 * <p>
 * Operation Log is dispatched to auth-service using mq, thus it's async and won't slow things much
 * <p>
 * This annotation is usually used for the web endpoints (but any method call is supported)
 * </p>
 * Usage example:
 * <pre>
 *    {@code
 *      @LogOperation(name = "/extension/name", description = "list supported file extensions")
 *      @GetMapping(path = "/extension/name", produces = MediaType.APPLICATION_JSON_VALUE)
 *      public Result<List<String>> listSupportedFileExtensionNames() {
 *          return Result.of(
 *              fileExtensionService.getNamesOfAllEnabled()
 *          );
 *      }
 *    }
 * </pre>
 *
 * @author yongjie.zhuang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
@Documented
public @interface LogOperation {

    /**
     * Operation's name
     */
    String name();

    /**
     * Operation's description
     * <p>
     * Default to ""
     * </p>
     */
    String description() default "";

    /**
     * Is the operation log enabled
     * <p>
     * Default to true, set to false to disable it
     * </p>
     */
    boolean enabled() default true;

    /**
     * Log parameters, by default true
     */
    boolean logParameters() default true;

}
