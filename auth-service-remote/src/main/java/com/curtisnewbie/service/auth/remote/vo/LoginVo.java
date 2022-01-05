package com.curtisnewbie.service.auth.remote.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yongjie.zhuang
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginVo {

    /** username */
    private String username;
    /** password */
    private String password;
    /** application name that the user is trying to use */
    private String appName;
}
