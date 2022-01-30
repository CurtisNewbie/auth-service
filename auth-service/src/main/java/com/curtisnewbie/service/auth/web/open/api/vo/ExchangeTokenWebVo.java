package com.curtisnewbie.service.auth.web.open.api.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yongjie.zhuang
 */
@Data
public class ExchangeTokenWebVo {

    @NotBlank
    private String token;
}
