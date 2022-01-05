package com.curtisnewbie.service.auth.remote.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author yongjie.zhuang
 */
@Data
@Builder
public class UpdatePasswordVo {

    /** new password (in plain text) */
    private String newPassword;

    /** old password (in plain text) */
    private String oldPassword;

    /** user's id */
    private long userId;
}
