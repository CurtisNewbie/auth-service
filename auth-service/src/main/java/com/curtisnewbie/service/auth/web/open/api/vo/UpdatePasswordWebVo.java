package com.curtisnewbie.service.auth.web.open.api.vo;

import lombok.Data;
import lombok.ToString;

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

    @ToString.Include(name = "prevPassword")
    public String prevPasswordMask() {
        return "****";
    }

    @ToString.Include(name = "newPassword")
    public String newPasswordMask() {
        return "****";
    }
}
