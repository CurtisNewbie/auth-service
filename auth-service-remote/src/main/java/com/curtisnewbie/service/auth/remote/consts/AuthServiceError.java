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
    USER_NOT_PERMITTED("AUTH0004", "Not permitted to use this system, please contact administrator if you want the permission"),
    USER_ALREADY_REGISTERED("AUTH0005", "User is already registered"),
    ADMIN_REG_NOT_ALLOWED("AUTH0006", "Not allowed to register as Admin"),
    TOKEN_EXPIRED("AUTH0007", "Token has expired"),
    REG_REVIEW_PENDING("AUTH0008", "Your Registration is being reviewed, please wait for approval"),
    REG_REVIEW_REJECTED("AUTH0009", "Your are not permitted to login, please contact administrator");

    private final String code;
    private final String msg;

}
