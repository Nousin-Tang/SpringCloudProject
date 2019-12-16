package com.nousin.springcloud.auth.framework.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

/**
 * 资源服务配置
 * 用于保护oauth相关的endpoints，同时主要作用于用户的登录(form login,Basic auth)
 *
 * @author tangwc
 * @since 2019/12/11
 */
@Configuration
@EnableResourceServer
@Order(3)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${nousin.base.request-path}")
    private String requestPath;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
//                .requestMatchers().antMatchers("/api/**")
//                .and()
                .authorizeRequests()
                .antMatchers(requestPath + "/**").authenticated()
                .and()
                .httpBasic();
    }
}
