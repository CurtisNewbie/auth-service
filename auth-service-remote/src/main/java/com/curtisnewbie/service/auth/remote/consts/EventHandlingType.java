package com.curtisnewbie.service.auth.remote.consts;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.curtisnewbie.common.enums.IntEnum;
import com.curtisnewbie.service.auth.remote.vo.UserRequestAppApprovalCmd;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Type of event that will be handled
 *
 * @author yongjie.zhuang
 */
public enum EventHandlingType implements IntEnum {

    /**
     * 1-registration event, the body is the id of user
     */
    REGISTRATION_EVENT(1),

    /**
     * 2-request app approval (user requesting approval for using the app), body is {@link UserRequestAppApprovalCmd}
     */
    REQUEST_APP_APPROVAL(2);

    @JsonValue
    @EnumValue
    public final int val;

    EventHandlingType(int v) {
        this.val = v;
    }

    @Override
    public int getValue() {
        return this.val;
    }
}
