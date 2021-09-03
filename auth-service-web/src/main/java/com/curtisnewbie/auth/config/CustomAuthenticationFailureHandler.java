package com.curtisnewbie.auth.config;

import com.curtisnewbie.common.util.JsonUtils;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.config.AuthenticationFailureHandlerExtender;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yongjie.zhuang
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandlerExtender {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {
        String errorMsg = "Incorrect credentials";
        if (e instanceof DisabledException) {
            errorMsg = "User is disabled";
        } else if (e instanceof InsufficientAuthenticationException) {
            errorMsg = "Only admin is allowed";
        }
        response.getWriter().write(JsonUtils.writeValueAsString(Result.error(errorMsg)));
    }
}
