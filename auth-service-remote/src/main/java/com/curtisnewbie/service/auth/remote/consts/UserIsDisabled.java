package com.curtisnewbie.service.auth.remote.consts;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.curtisnewbie.common.enums.IntEnum;
import com.curtisnewbie.common.util.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
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

    @JsonCreator
    public static UserIsDisabled from(Integer v) {
        if (v == null) return null;
        return EnumUtils.parse(v, UserIsDisabled.class);
    }
}
