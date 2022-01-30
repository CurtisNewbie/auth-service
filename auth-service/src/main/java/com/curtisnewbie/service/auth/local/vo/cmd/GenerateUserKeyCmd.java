package com.curtisnewbie.service.auth.local.vo.cmd;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

/**
 * @author yongjie.zhuang
 */
@Data
@Builder
public class GenerateUserKeyCmd {

    private final int userId;
    @NonNull
    private final LocalDateTime expirationTime;
    private final String createBy;
}
