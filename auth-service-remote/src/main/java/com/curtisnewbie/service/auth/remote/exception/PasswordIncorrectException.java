package com.curtisnewbie.service.auth.remote.exception;

/**
 * User's password is incorrect
 *
 * @author yongjie.zhuang
 */
public class PasswordIncorrectException extends UserRelatedException {

    public PasswordIncorrectException() {

    }

    public PasswordIncorrectException(String m) {
        super("Password Incorrect");
    }
}
