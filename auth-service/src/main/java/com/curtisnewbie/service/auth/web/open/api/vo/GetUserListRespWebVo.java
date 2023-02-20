package com.curtisnewbie.service.auth.web.open.api.vo;

import com.curtisnewbie.common.vo.PageableVo;
import com.curtisnewbie.service.auth.remote.vo.UserInfoVo;
import lombok.Data;

import java.io.Serializable;

/**
 * Get user list response vo
 *
 * @author yongjie.zhuang
 */
@Data
public class GetUserListRespWebVo extends PageableVo<Void> {

    private Iterable<UserInfoVo> list;
}
