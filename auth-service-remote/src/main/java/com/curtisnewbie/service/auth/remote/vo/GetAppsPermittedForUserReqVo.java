package com.curtisnewbie.service.auth.remote.vo;

import com.curtisnewbie.common.vo.PageableVo;
import lombok.Data;

/**
 * @author yongjie.zhuang
 */
@Data
public class GetAppsPermittedForUserReqVo extends PageableVo<Void> {

    /**
     * User's id
     */
    private int userId;

}
