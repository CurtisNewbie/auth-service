package com.curtisnewbie.service.auth.web.open.api.vo;

import lombok.*;

import javax.validation.constraints.*;

/**
 * @author yongjie.zhuang
 */
@Data
public class DeleteUserKeyReqVo {

    @NotNull
    private Integer userKeyId;

}
