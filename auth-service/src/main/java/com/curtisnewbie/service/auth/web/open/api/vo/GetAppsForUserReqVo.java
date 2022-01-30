package com.curtisnewbie.service.auth.web.open.api.vo;

import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.ValidUtils;
import lombok.Data;

/**
 * @author yongjie.zhuang
 */
@Data
public class GetAppsForUserReqVo {

    /**
     * User's id
     */
    private Integer userId;

    public void validate() throws MsgEmbeddedException {
        ValidUtils.requireNonNull(getUserId());
    }
}
