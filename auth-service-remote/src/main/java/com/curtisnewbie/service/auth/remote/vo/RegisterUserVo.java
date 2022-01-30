package com.curtisnewbie.service.auth.remote.vo;


import com.curtisnewbie.service.auth.remote.consts.UserRole;
import lombok.*;

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

    @ToString.Include(name = "password")
    public String passwordMask() {
        return "****";
    }
}
