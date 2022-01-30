package com.curtisnewbie.service.auth.remote.exception;

/**
 * User is disabled
 *
 * @author yongjie.zhuang
 */
public class UserDisabledException extends UserRelatedException {

    public UserDisabledException() {

    }

    public UserDisabledException(String m) {
        super("User is disabled");
    }
}
