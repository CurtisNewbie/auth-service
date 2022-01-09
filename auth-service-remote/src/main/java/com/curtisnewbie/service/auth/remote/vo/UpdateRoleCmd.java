package com.curtisnewbie.service.auth.remote.vo;

import com.curtisnewbie.service.auth.remote.consts.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author yongjie.zhuang
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateRoleCmd {

    /** user's id */
    private int id;

    @NotNull
    private UserRole role;

    private String updatedBy;

}
