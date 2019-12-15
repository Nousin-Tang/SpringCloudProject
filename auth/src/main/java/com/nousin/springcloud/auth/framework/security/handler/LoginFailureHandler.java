package com.nousin.springcloud.auth.framework.security.handler;

import com.alibaba.fastjson.JSON;
import com.nousin.springcloud.auth.framework.common.dto.ResultDto;
import com.nousin.springcloud.auth.framework.common.util.ResultUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

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
        response.getWriter().write(JSON.toJSONString(ResultUtil.success("认证失败","")));
    }
}
