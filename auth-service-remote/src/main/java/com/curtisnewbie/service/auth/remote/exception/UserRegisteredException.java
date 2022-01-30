package com.curtisnewbie.service.auth.remote.exception;

/**
 * User with the same name is already registered
 *
 * @author yongjie.zhuang
 */
public class UserRegisteredException extends UserRelatedException {

    public UserRegisteredException() {

    }

    public UserRegisteredException(String m) {
        super("User with the given name is already registered");
    }
}
