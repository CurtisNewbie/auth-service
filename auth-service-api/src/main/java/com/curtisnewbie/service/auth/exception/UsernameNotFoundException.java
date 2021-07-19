package com.curtisnewbie.service.auth.exception;

/**
 * Exception indicating that username is not found
 *
 * @author yongjie.zhuang
 */
public class UsernameNotFoundException extends UserRelatedException {

    public UsernameNotFoundException(String msg) {
        super(msg);
    }
}
