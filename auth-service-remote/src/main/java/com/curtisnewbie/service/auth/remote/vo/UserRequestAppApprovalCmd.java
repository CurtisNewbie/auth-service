package com.curtisnewbie.service.auth.remote.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author yongjie.zhuang
 */
@Data
@Builder
public class UserRequestAppApprovalCmd {

    /** user_id */
    private Integer userId;

    /** app_id */
    private Integer appId;

    public boolean isInvalid() {
        return userId == null || appId == null;
    }
}
