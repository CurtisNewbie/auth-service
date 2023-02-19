package com.curtisnewbie.service.auth.remote.vo;


import lombok.*;

import javax.validation.constraints.NotBlank;
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
public class AddUserVo implements Serializable {

    @NotBlank
    private String username;

    /** password (in plain text) */
    @NotBlank
    private String password;

    @NotBlank
    private String roleNo;

    @ToString.Include(name = "password")
    public String passwordMask() {
        return "****";
    }
}
