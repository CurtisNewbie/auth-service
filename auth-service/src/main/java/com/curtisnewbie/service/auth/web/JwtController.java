package com.curtisnewbie.service.auth.web;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.aop.LogOperation;
import com.curtisnewbie.module.jwt.domain.api.JwtBuilder;
import com.curtisnewbie.module.jwt.domain.api.JwtDecoder;
import com.curtisnewbie.module.jwt.vo.DecodeResult;
import com.curtisnewbie.service.auth.local.api.LocalUserService;
import com.curtisnewbie.service.auth.remote.exception.UserRelatedException;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import com.curtisnewbie.service.auth.vo.ExchangeTokenWebVo;
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

    @Autowired
    private JwtDecoder jwtDecoder;

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

    @LogOperation(name = "/token/exchange-token", description = "exchange token")
    @PostMapping("/exchange-token")
    public Result<String> exchangeToken(@Validated @RequestBody ExchangeTokenWebVo exchangeTokenWebVo) {

        DecodeResult decodeResult = jwtDecoder.decode(exchangeTokenWebVo.getToken());
        if (!decodeResult.isValid()) {
            return Result.error("Session is expired, please login first");
        }

        DecodedJWT decodedJWT = decodeResult.getDecodedJWT();
        Map<String, String> claims = new HashMap<>();
        claims.put("id", decodedJWT.getClaim("id").asString());
        claims.put("username", decodedJWT.getClaim("username").asString());
        claims.put("role", decodedJWT.getClaim("role").asString());

        // valid for 20 minutes
        return Result.of(jwtBuilder.encode(claims, LocalDateTime.now().plusMinutes(20)));
    }
}
