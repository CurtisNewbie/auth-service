package com.curtisnewbie.service.auth.web.open.api.boundary;

import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.aop.LogOperation;
import com.curtisnewbie.service.auth.local.api.LocalUserService;
import com.curtisnewbie.service.auth.remote.exception.UserRelatedException;
import com.curtisnewbie.service.auth.web.open.api.vo.ExchangeTokenWebVo;
import com.curtisnewbie.service.auth.web.open.api.vo.LoginWebVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private LocalUserService userService;

    @LogOperation(name = "/token/login-for-token", description = "get token", enabled = false)
    @PostMapping("/login-for-token")
    public Result<String> getToken(@Validated @RequestBody LoginWebVo loginWebVo) throws UserRelatedException {
        return Result.of(userService.exchangeToken(loginWebVo.getUsername(), loginWebVo.getPassword()));
    }

    @LogOperation(name = "/token/exchange-token", description = "exchange token", enabled = false)
    @PostMapping("/exchange-token")
    public Result<String> exchangeToken(@Validated @RequestBody ExchangeTokenWebVo exchangeTokenWebVo) {
        return Result.of(userService.exchangeToken(exchangeTokenWebVo.getToken()));
    }
}
