package com.curtisnewbie.service.auth.vo;

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
