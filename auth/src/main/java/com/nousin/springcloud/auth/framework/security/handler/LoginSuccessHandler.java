package com.nousin.springcloud.auth.framework.security.handler;

import com.alibaba.fastjson.JSON;
import com.nousin.springcloud.common.util.ResultUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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
@Component("loginSuccessHandler")
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        System.err.println(JSON.toJSONString(authentication));
        response.getWriter().write(JSON.toJSONString(ResultUtil.success("认证成功", authentication.getCredentials())));
    }
}
