package com.curtisnewbie.service.auth.web;

import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.aop.LogOperation;
import com.curtisnewbie.module.jwt.domain.api.JwtBuilder;
import com.curtisnewbie.service.auth.local.api.LocalUserService;
import com.curtisnewbie.service.auth.remote.exception.UserRelatedException;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import com.curtisnewbie.service.auth.vo.LoginWebVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for JWT retrieval and exchange
 *
 * @author yongjie.zhuang
 */
@Slf4j
@RestController
@RequestMapping("${web.base-path}/token")
public class JwtController {

    @Autowired
    private LocalUserService remoteUserService;

    @Autowired
    private JwtBuilder jwtBuilder;

    @LogOperation(name = "/token/login-for-token", description = "get token")
    @PostMapping("/login-for-token")
    public Result<String> getToken(@Validated @RequestBody LoginWebVo loginWebVo) throws UserRelatedException {
        UserVo user = remoteUserService.login(loginWebVo.getUsername(), loginWebVo.getPassword());
        Map<String, String> claims = new HashMap<>();
        claims.put("id", user.getId().toString());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());

        // valid for 20 minutes
        return Result.of(jwtBuilder.encode(claims, LocalDateTime.now().plusMinutes(20)));
    }
}
