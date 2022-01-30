package com.curtisnewbie.service.auth.remote.exception;

/**
 * The maximum number of administrators is exceeded
 *
 * @author yongjie.zhuang
 */
public class ExceededMaxAdminCountException extends UserRelatedException {

    public ExceededMaxAdminCountException(String msg) {
        super(msg);
    }

    public ExceededMaxAdminCountException() {
        super("Maximum number of admin is exceeded");
    }

}
