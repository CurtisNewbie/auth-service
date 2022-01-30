package com.curtisnewbie.service.auth.remote.exception;

/**
 * Exception indicating that the Authentication is currently invalid
 *
 * @author yongjie.zhuang
 */
public class InvalidAuthenticationException extends UserRelatedException {

    public InvalidAuthenticationException(String msg) {
        super(msg);
    }

    public InvalidAuthenticationException() {
        super("Please login first");
    }
}
