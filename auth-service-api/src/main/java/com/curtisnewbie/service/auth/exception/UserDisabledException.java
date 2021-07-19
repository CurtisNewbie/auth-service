package com.curtisnewbie.service.auth.exception;

/**
 * User is disabled
 *
 * @author yongjie.zhuang
 */
public class UserDisabledException extends UserRelatedException {

    public UserDisabledException() {

    }

    public UserDisabledException(String m) {
        super(m);
    }
}
