package com.curtisnewbie.service.auth.web;

import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.aop.LogOperation;
import com.curtisnewbie.module.jwt.domain.api.JwtBuilder;
import com.curtisnewbie.service.auth.remote.api.RemoteUserService;
import com.curtisnewbie.service.auth.remote.exception.UserRelatedException;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import com.curtisnewbie.service.auth.vo.LoginWebVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Controller for JWT retrieval and exchange
 *
 * @author yongjie.zhuang
 */
@Slf4j
@RestController
@RequestMapping("${web.base-path}/token")
public class JwtController {

    @DubboReference
    private RemoteUserService remoteUserService;

    @Autowired
    private JwtBuilder jwtBuilder;

    @LogOperation(name = "/token/login-for-token", description = "get token")
    @PostMapping("/login-for-token")
    public Result<String> getToken(@Validated @RequestBody LoginWebVo loginWebVo) throws UserRelatedException {
        UserVo user = remoteUserService.login(loginWebVo.getUsername(), loginWebVo.getPassword());

        // valid for 20 minutes
        return Result.of(jwtBuilder.encode(user, LocalDateTime.now().plusMinutes(20)));
    }
}
