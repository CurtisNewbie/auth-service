package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.service.auth.local.vo.cmd.GenerateUserKeyCmd;
import com.curtisnewbie.service.auth.local.vo.resp.GenerateUserKeyResp;
import org.springframework.validation.annotation.Validated;

/**
 * Service for user's key
 *
 * @author yongjie.zhuang
 */
@Validated
public interface LocalUserKeyService {

    /**
     * Generate a new secret key for user
     */
    GenerateUserKeyResp generateUserKey(GenerateUserKeyCmd cmd);
}
