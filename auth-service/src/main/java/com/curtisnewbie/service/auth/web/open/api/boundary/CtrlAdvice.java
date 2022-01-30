package com.curtisnewbie.service.auth.web.open.api.boundary;

import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.remote.exception.ExceededMaxAdminCountException;
import com.curtisnewbie.service.auth.remote.exception.InvalidAuthenticationException;
import com.curtisnewbie.service.auth.remote.exception.PasswordIncorrectException;
import com.curtisnewbie.service.auth.remote.exception.UserRegisteredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yongjie.zhuang
 */
@ControllerAdvice
public class CtrlAdvice {

    private final Logger logger = LoggerFactory.getLogger(CtrlAdvice.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<?> handleGeneralException(Exception e) {
        logger.error("Exception occurred", e);
        return Result.error("Internal Error");
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseBody
    public ResponseEntity<Result<?>> handleAccessDeniedException(Exception e) {
        return wrapResponse(Result.error("Operation not allowed"));
    }

    @ExceptionHandler({ExceededMaxAdminCountException.class})
    @ResponseBody
    public Result<?> handleExceededMaxAminCountException(Exception e) {
        return Result.error("Maximum number of admin is exceeded");
    }

    @ExceptionHandler({UserRegisteredException.class})
    @ResponseBody
    public Result<?> handleUserRegisteredException(Exception e) {
        return Result.error("User registered already");
    }

    @ExceptionHandler({InvalidAuthenticationException.class})
    @ResponseBody
    public ResponseEntity<Result<?>> handleInvalidAuthenticationException(Exception e) {
        return wrapResponse(Result.error("Please login first"));
    }

    @ExceptionHandler({PasswordIncorrectException.class})
    @ResponseBody
    public ResponseEntity<Result<?>> handlePasswordIncorrectException(Exception e) {
        return wrapResponse(Result.error("Password Incorrect"));
    }

    @ExceptionHandler({MsgEmbeddedException.class})
    @ResponseBody
    public Result<?> handleMsgEmbeddedException(MsgEmbeddedException e) {
        String errorMsg = e.getMsg();
        if (!StringUtils.hasText(errorMsg)) {
            errorMsg = "Invalid parameters";
        }
        return Result.error(errorMsg);
    }

    private static ResponseEntity<Result<?>> wrapResponse(Result<?> result) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }
}
