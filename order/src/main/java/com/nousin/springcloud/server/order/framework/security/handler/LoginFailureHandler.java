package com.nousin.springcloud.server.order.framework.security.handler;

import com.alibaba.fastjson.JSON;
import com.nousin.springcloud.server.order.framework.common.dto.ResultDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TODO
 *
 * @author tangwc
 * @since 2019/12/11
 */
@Component("loginFailureHandler")
public class LoginFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        response.getWriter().write(JSON.toJSONString(new ResultDto("0","认证成功","")));
    }
}
