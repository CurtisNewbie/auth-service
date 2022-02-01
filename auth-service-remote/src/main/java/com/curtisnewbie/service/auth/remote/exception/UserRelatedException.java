package com.curtisnewbie.service.auth.remote.exception;

import com.curtisnewbie.common.exceptions.UnrecoverableException;

/**
 * Exceptions related to user
 *
 * @author yongjie.zhuang
 */
public class UserRelatedException extends UnrecoverableException {

    public UserRelatedException(String msg) {
        super(msg);
    }

    public UserRelatedException(String msg, Throwable e) {
        super(msg, e);
    }

    public UserRelatedException(String msgInStacktrace, String customMsg, Throwable e) {
        super(msgInStacktrace, customMsg, e);
    }

    public UserRelatedException() {
    }
}
