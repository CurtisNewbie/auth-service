package com.curtisnewbie.service.auth.remote.consts;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.curtisnewbie.common.enums.IntEnum;

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
    private final int val;

    EventHandlingResult(int v) {
        this.val = v;
    }

    @Override
    public int getValue() {
        return val;
    }
}
