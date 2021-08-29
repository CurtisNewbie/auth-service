package com.curtisnewbie.service.auth.remote.consts;

import com.curtisnewbie.common.enums.IntEnum;

/**
 * Type of event that will be handled
 *
 * @author yongjie.zhuang
 */
public enum EventHandlingType implements IntEnum {

    /**
     * 1-registration event
     */
    REGISTRATION_EVENT(1);

    public final int val;

    EventHandlingType(int v) {
        this.val = v;
    }

    @Override
    public int getValue() {
        return this.val;
    }
}
