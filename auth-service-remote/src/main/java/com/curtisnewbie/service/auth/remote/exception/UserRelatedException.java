package com.curtisnewbie.service.auth.remote.exception;

import com.curtisnewbie.common.exceptions.MsgEmbeddedException;

/**
 * Exceptions related to user
 *
 * @author yongjie.zhuang
 */
public class UserRelatedException extends MsgEmbeddedException  {

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
