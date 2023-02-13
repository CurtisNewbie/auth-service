package com.curtisnewbie.service.auth.remote.vo;

import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author yongjie.zhuang
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserVo implements Serializable {

    /** primary key */
    private int id;

    /** role */
    @NotNull
    private UserRole role;

    /** whether the user is disabled, 0-normal, 1-disabled */
    @NotNull
    private UserIsDisabled isDisabled;

    /** who updated this user */
    @NotEmpty
    private String updateBy;

    /** userNo */
    @NotEmpty
    private String roleNo;

}
