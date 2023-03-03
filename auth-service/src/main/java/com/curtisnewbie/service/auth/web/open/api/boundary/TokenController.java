package com.curtisnewbie.service.auth.web.open.api.boundary;

import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.goauth.client.PathDoc;
import com.curtisnewbie.goauth.client.PathType;
import com.curtisnewbie.service.auth.dao.User;
import com.curtisnewbie.service.auth.local.api.UserService;
import com.curtisnewbie.service.auth.web.open.api.vo.ExchangeTokenWebVo;
import com.curtisnewbie.service.auth.web.open.api.vo.UserWebVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * Controller for JWT retrieval and exchange
 *
 * @author yongjie.zhuang
 */
@Slf4j
@RestController
@RequestMapping("${web.base-path}/token")
public class TokenController {

    @Autowired
    private UserService userService;

    @PathDoc(description = "Exchange token", type = PathType.PUBLIC)
    @PostMapping("/exchange")
    public Result<String> exchangeToken(@Validated @RequestBody ExchangeTokenWebVo exchangeTokenWebVo) {
        return Result.of(userService.exchangeToken(exchangeTokenWebVo.getToken()));
    }

    @PathDoc(description = "Get user info by token", type = PathType.PUBLIC)
    @GetMapping("/user")
    public Result<UserWebVo> getUserInfo(@RequestParam("token") final String token) {
        final User userInfo = userService.getUserInfoByToken(token);
        return Result.of(BeanCopyUtils.toType(userInfo, UserWebVo.class));
    }
}
