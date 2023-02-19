package com.curtisnewbie.service.auth.remote.vo;

import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Find user info vo
 *
 * @author yongjie.zhuang
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FindUserInfoVo implements Serializable {

    /** username */
    private String username;

    /** whether the user is disabled */
    private UserIsDisabled isDisabled;

    /** paging param */
    private PagingVo pagingVo = new PagingVo();

}
