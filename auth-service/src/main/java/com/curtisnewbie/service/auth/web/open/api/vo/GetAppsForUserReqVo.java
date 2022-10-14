package com.curtisnewbie.service.auth.web.open.api.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yongjie.zhuang
 */
@Data
public class GetAppsForUserReqVo {

    /**
     * User's id
     */
    @NotNull
    private Integer userId;
}
