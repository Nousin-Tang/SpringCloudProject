package com.nousin.springcloud.server.order.framework.security.handler;

import com.alibaba.fastjson.JSON;
import com.nousin.springcloud.server.order.framework.common.dto.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
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
@Component("loginSuccessHandler")
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        System.err.println(JSON.toJSONString(authentication));
        response.getWriter().write(JSON.toJSONString(new ResultDto("0","认证成功",authentication.getCredentials())));
    }
}
