package com.curtisnewbie.service.auth.web.open.api.boundary;

import com.curtisnewbie.common.trace.TUser;
import com.curtisnewbie.common.trace.TraceUtils;
import com.curtisnewbie.common.vo.Result;
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

import static com.curtisnewbie.common.util.AssertUtils.isTrue;

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

    @PostMapping("/generate")
    public Result<GenerateUserKeyRespWebVo> generateUserKey(@Validated @RequestBody GenerateUserKeyReqWebVo req) throws InvalidAuthenticationException,
            UserDisabledException, UsernameNotFoundException {

        // before we generate a secret key for current user, we do a password validation
        TUser tUser = TraceUtils.tUser();
        final String username = tUser.getUsername();
        final boolean isCorrect = userService.validateUserPassword(username, req.getPassword());
        isTrue(isCorrect, "Password incorrect, unable to generate user secret key");

        final GenerateUserKeyResp resp = userKeyService.generateUserKey(GenerateUserKeyCmd.builder()
                .userId(tUser.getUserId())
                .createBy(username)
                .expirationTime(LocalDateTime.now().plusMonths(3)) // by default expire in three months
                .build());

        return Result.of(GenerateUserKeyRespWebVo.builder()
                .expirationTime(resp.getExpirationTime())
                .key(resp.getKey())
                .build());
    }
}
