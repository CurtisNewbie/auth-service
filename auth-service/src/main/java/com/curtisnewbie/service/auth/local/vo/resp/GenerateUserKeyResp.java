package com.curtisnewbie.service.auth.local.vo.resp;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

/**
 * @author yongjie.zhuang
 */
@Data
@Builder
public class GenerateUserKeyResp {

    private int userId;

    @NonNull
    private String key;

    @NonNull
    private LocalDateTime expirationTime;
}
