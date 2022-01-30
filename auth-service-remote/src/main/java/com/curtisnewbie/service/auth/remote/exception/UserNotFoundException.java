package com.curtisnewbie.service.auth.remote.exception;

/**
 * User is not found
 *
 * @author yongjie.zhuang
 */
public class UserNotFoundException extends UserRelatedException {

    public UserNotFoundException() {

    }

    public UserNotFoundException(String m) {
        super("User is not found");
    }
}
