package com.curtisnewbie.service.auth.web.open.api.boundary;

import com.curtisnewbie.common.trace.TUser;
import com.curtisnewbie.common.trace.TraceUtils;
import com.curtisnewbie.common.vo.*;
import com.curtisnewbie.service.auth.local.api.UserKeyService;
import com.curtisnewbie.service.auth.local.api.UserService;
import com.curtisnewbie.service.auth.local.vo.cmd.GenerateUserKeyCmd;
import com.curtisnewbie.service.auth.local.vo.resp.GenerateUserKeyResp;
import com.curtisnewbie.service.auth.remote.exception.InvalidAuthenticationException;
import com.curtisnewbie.service.auth.remote.exception.UserDisabledException;
import com.curtisnewbie.service.auth.remote.exception.UsernameNotFoundException;
import com.curtisnewbie.service.auth.web.open.api.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private UserKeyService userKeyService;
    @Autowired
    private UserService userService;

    /**
     * Generate user key, the user key can be used as a password
     */
    @PostMapping("/generate")
    public Result<Void> generateUserKey(@Validated @RequestBody GenerateUserKeyReqWebVo req) {

        // before we generate a secret key for current user, we do a password validation
        TUser tUser = TraceUtils.tUser();
        final String username = tUser.getUsername();
        final boolean isCorrect = userService.validateUserPassword(username, req.getPassword());
        isTrue(isCorrect, "Password incorrect, unable to generate user secret key");

        userKeyService.generateUserKey(GenerateUserKeyCmd.builder()
                .userId(tUser.getUserId())
                .name(req.getKeyName())
                .expirationTime(LocalDateTime.now().plusMonths(3)) // by default expire in three months
                .build());

        return Result.ok();
    }

    /**
     * List user keys
     */
    @PostMapping("/list")
    public Result<PageableList<UserKeyVo>> listUserKeys(@RequestBody PagingVo p) {
        final TUser user = TraceUtils.tUser();
        return Result.of(userKeyService.listUserKeys(user.getUserId(), p));
    }

    /**
     * Generate a random user key
     */
    @PostMapping("/key/generate")
    public Result<Void> generateRandomUserKey(@RequestParam String name) {
        final TUser user = TraceUtils.tUser();
        userKeyService.generateUserKey(GenerateUserKeyCmd.builder()
                .userId(user.getUserId())
                .expirationTime(LocalDateTime.now().plusMonths(3))
                .build());
        return Result.ok();
    }
}
