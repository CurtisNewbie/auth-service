package com.curtisnewbie.service.auth.remote.consts;

import com.curtisnewbie.common.util.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Auth-service error types
 *
 * @author yongjie.zhuang
 */
@Getter
@AllArgsConstructor
public enum AuthServiceError implements ErrorType {

    USER_NOT_FOUND("AUTH0001", "User not found"),
    PASSWORD_INCORRECT("AUTH0002", "Password incorrect"),
    USER_DISABLED("AUTH0003", "User disabled"),
    USER_NOT_PERMITTED("AUTH0004", "Not permitted to use this functionality"),
    USER_ALREADY_REGISTERED("AUTH0005", "User is already registered"),
    ADMIN_REG_NOT_ALLOWED("AUTH0006", "Not allowed to register as Admin"),
    TOKEN_EXPIRED("AUTH0007", "Token has expired");


    private final String code;
    private final String msg;

}
