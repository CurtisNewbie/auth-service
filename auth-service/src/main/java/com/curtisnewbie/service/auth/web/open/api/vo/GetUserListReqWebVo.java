package com.curtisnewbie.service.auth.web.open.api.vo;

import com.curtisnewbie.common.vo.PageableVo;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import lombok.Data;

import java.io.Serializable;

/**
 * Get user list request vo
 *
 * @author yongjie.zhuang
 */
@Data
public class GetUserListReqWebVo extends PageableVo implements Serializable {

    /**
     * Username
     */
    private String username;

    /**
     * role
     */
    private UserRole role;

    /**
     * is user disabled
     */
    private UserIsDisabled isDisabled;

}
