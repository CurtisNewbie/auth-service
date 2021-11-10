package com.curtisnewbie.service.auth.remote.vo;

import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Find user info vo
 *
 * @author yongjie.zhuang
 */
@Data
public class FindUserInfoVo implements Serializable {

    /** username */
    private String username;

    /** role */
    private UserRole role;

    /** whether the user is disabled */
    private UserIsDisabled isDisabled;

    /** paging param */
    @NotNull
    private PagingVo pagingVo;

}
