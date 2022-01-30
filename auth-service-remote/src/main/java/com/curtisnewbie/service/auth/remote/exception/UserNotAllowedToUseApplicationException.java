package com.curtisnewbie.service.auth.remote.exception;

/**
 * User is not allowed to use the specified application
 *
 * @author yongjie.zhuang
 */
public class UserNotAllowedToUseApplicationException extends UserRelatedException {

    public UserNotAllowedToUseApplicationException() {

    }

    public UserNotAllowedToUseApplicationException(String msg) {
        super("You are not allowed to use this application");
    }

    public UserNotAllowedToUseApplicationException(String msg, Throwable t) {
        super(msg, t);
    }

}
