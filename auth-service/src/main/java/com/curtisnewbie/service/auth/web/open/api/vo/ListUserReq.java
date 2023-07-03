package com.curtisnewbie.service.auth.web.open.api.vo;

import com.curtisnewbie.common.vo.PageableVo;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Get user list request vo
 *
 * @author yongjie.zhuang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ListUserReq extends PageableVo<Void> {

    private String username;
    private String roleNo;
    private UserIsDisabled isDisabled;
}
