package com.curtisnewbie.service.auth.remote.consts;

import com.curtisnewbie.common.enums.IntEnum;

/**
 * Status of event, 0-no need to handle, 1-to be handled, 2-handled
 *
 * @author yongjie.zhuang
 */
public enum EventHandlingStatus implements IntEnum {

    /**
     * 0-No need to handle
     */
    NO_NEED_TO_HANDLE(0),

    /**
     * 1-to be handled
     */
    TO_BE_HANDLED(1),

    /**
     * 2-handled already
     */
    HANDLED(2);

    public final int val;

    EventHandlingStatus(int v) {
        this.val = v;
    }

    @Override
    public int getValue() {
        return val;
    }
}
