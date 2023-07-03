package com.curtisnewbie.service.auth.web.open.api.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author yongjie.zhuang
 */
@Data
@ToString(exclude = { "prevPassword", "newPassword" })
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
