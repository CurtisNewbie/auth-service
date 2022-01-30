package com.curtisnewbie.service.auth.web.open.api.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

/**
 * @author yongjie.zhuang
 */
@Data
@Builder
public class GenerateUserKeyRespWebVo {

    @NonNull
    private String key;

    @NonNull
    private LocalDateTime expirationTime;
}
