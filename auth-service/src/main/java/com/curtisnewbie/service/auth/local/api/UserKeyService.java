package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.common.vo.*;
import com.curtisnewbie.service.auth.local.vo.cmd.GenerateUserKeyCmd;
import com.curtisnewbie.service.auth.local.vo.resp.GenerateUserKeyResp;
import com.curtisnewbie.service.auth.web.open.api.vo.*;
import org.springframework.lang.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.*;
import javax.validation.constraints.*;

/**
 * Service for user's key
 *
 * @author yongjie.zhuang
 */
@Validated
public interface UserKeyService {

    /**
     * Generate a new secret key for user
     */
    GenerateUserKeyResp generateUserKey(@NotNull @Valid GenerateUserKeyCmd cmd);

    /**
     * List user keys
     */
    PageableList<UserKeyVo> listUserKeys(int userId, @Nullable String name, @NotNull PagingVo p);

    /**
     * Mark user key deleted
     */
    void deleteUserKey(int userId, int keyId);

    /**
     * Validate user key
     */
    boolean isUserKeyValid(int userId, @Nullable String key);
}
