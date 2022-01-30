package com.curtisnewbie.service.auth.remote.consts;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.curtisnewbie.common.enums.IntEnum;
import com.curtisnewbie.common.util.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>
 * Result of event handling
 * </p>
 *
 * @author yongjie.zhuang
 */
public enum EventHandlingResult implements IntEnum {

    /**
     * Accept
     */
    ACCEPT(1),

    /**
     * Reject
     */
    REJECT(2);

    @EnumValue
    @JsonValue
    private final int val;

    EventHandlingResult(int v) {
        this.val = v;
    }

    @Override
    public int getValue() {
        return val;
    }

    @JsonCreator
    public static EventHandlingResult from(Integer v) {
        if (v == null) return null;
        return EnumUtils.parse(v, EventHandlingResult.class);
    }
}

