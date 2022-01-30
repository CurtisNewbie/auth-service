package com.curtisnewbie.service.auth.web.open.api.boundary;

import com.curtisnewbie.common.exceptions.UnrecoverableMsgEmbeddedException;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.aop.LogOperation;
import com.curtisnewbie.module.auth.util.AuthUtil;
import com.curtisnewbie.service.auth.local.api.LocalUserKeyService;
import com.curtisnewbie.service.auth.local.api.LocalUserService;
import com.curtisnewbie.service.auth.local.vo.cmd.GenerateUserKeyCmd;
import com.curtisnewbie.service.auth.local.vo.resp.GenerateUserKeyResp;
import com.curtisnewbie.service.auth.remote.exception.InvalidAuthenticationException;
import com.curtisnewbie.service.auth.remote.exception.UserDisabledException;
import com.curtisnewbie.service.auth.remote.exception.UsernameNotFoundException;
import com.curtisnewbie.service.auth.web.open.api.vo.GenerateUserKeyReqWebVo;
import com.curtisnewbie.service.auth.web.open.api.vo.GenerateUserKeyRespWebVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@RestController
@RequestMapping("${web.base-path}/user/key")
public class UserKeyController {

    @Autowired
    private LocalUserKeyService userKeyService;
    @Autowired
    private LocalUserService userService;

    @LogOperation(name = "/user/key/generate", description = "Generate user key")
    @PostMapping("/generate")
    public Result<GenerateUserKeyRespWebVo> generateUserKey(@Validated @RequestBody GenerateUserKeyReqWebVo req) throws InvalidAuthenticationException,
            UserDisabledException, UsernameNotFoundException {

        // before we generate a secret key for current user, we do a password validation
        final String username = AuthUtil.getUsername();
        final boolean isCorrect = userService.validateUserPassword(username, req.getPassword());
        if (!isCorrect) {
            throw new UnrecoverableMsgEmbeddedException("Password incorrect, unable to generate user secret key");
        }

        final GenerateUserKeyResp resp = userKeyService.generateUserKey(GenerateUserKeyCmd.builder()
                .userId(AuthUtil.getUserId())
                .createBy(username)
                .expirationTime(LocalDateTime.now().plusMonths(3)) // by default expire in three months
                .build());

        return Result.of(GenerateUserKeyRespWebVo.builder()
                .expirationTime(resp.getExpirationTime())
                .key(resp.getKey())
                .build());
    }
}
