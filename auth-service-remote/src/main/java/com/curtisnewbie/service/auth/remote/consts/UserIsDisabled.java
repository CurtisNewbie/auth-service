package com.curtisnewbie.service.auth.remote.consts;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.curtisnewbie.common.enums.IntEnum;
import com.fasterxml.jackson.annotation.JsonValue;

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

    @JsonValue
    @EnumValue
    private final int v;

    UserIsDisabled(int v) {
        this.v = v;
    }

    public int getValue() {
        return v;
    }
}
