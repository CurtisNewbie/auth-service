package com.curtisnewbie.auth.vo;

import lombok.Data;

/**
 * @author yongjie.zhuang
 */
@Data
public class UpdatePasswordVo {

    /**
     * Previous password
     */
    private String prevPassword;

    /**
     * New password
     */
    private String newPassword;
}
