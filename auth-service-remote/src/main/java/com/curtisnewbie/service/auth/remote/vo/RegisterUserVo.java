package com.curtisnewbie.service.auth.remote.vo;


import com.curtisnewbie.service.auth.remote.consts.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Vo for registering new user
 *
 * @author yongjie.zhuang
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterUserVo implements Serializable {

    /**
     * username
     */
    @NotBlank
    private String username;

    /**
     * password (in plain text)
     */
    @NotBlank
    private String password;

    /**
     * role
     */
    @NotNull
    private UserRole role;

    /**
     * create by
     */
    private String createBy;
}
