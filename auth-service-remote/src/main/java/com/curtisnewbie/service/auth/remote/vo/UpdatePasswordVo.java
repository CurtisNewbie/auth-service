package com.curtisnewbie.service.auth.remote.vo;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yongjie.zhuang
 */
@Data
@Builder
public class UpdatePasswordVo {

    /** new password (in plain text) */
    @NotBlank
    private String newPassword;

    /** old password (in plain text) */
    @NotNull
    private String oldPassword;

    /** user's id */
    private int userId;
}
