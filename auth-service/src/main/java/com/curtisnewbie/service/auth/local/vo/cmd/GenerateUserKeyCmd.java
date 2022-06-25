package com.curtisnewbie.service.auth.local.vo.cmd;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * @author yongjie.zhuang
 */
@Data
@Builder
public class GenerateUserKeyCmd {

    private final int userId;

    @NotEmpty
    private final String name;

    @NonNull
    private final LocalDateTime expirationTime;
}
