package com.curtisnewbie.service.auth.web.open.api.vo;

import lombok.Data;

/**
 * @author yongjie.zhuang
 */
@Data
public class UpdatePasswordWebVo {

    /**
     * Previous password
     */
    private String prevPassword;

    /**
     * New password
     */
    private String newPassword;
}
