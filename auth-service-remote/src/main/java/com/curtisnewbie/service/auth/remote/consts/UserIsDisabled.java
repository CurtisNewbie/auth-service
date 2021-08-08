package com.curtisnewbie.service.auth.remote.consts;

import com.curtisnewbie.common.enums.IntEnum;

/**
 * User isDisabled enum
 *
 * @author yongjie.zhuang
 */
public enum UserIsDisabled implements IntEnum {

    /** normal */
    NORMAL(0),

    /** user is disabled */
    DISABLED(1);

    private final int v;

    UserIsDisabled(int v) {
        this.v = v;
    }

    public int getValue() {
        return v;
    }
}
