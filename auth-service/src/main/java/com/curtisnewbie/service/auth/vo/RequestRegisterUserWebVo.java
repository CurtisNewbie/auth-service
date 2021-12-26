package com.curtisnewbie.service.auth.vo;

import lombok.Data;

/**
 * @author yongjie.zhuang
 */
@Data
public class RequestRegisterUserWebVo {

    /**
     * username
     */
    private String username;

    /**
     * password (in plain text)
     */
    private String password;
}
