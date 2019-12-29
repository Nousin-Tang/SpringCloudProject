package com.nousin.springcloud.server.storage.framework.security.config;

import com.alibaba.fastjson.JSONObject;
import com.nousin.springcloud.common.dto.OauthUserInfoDto;
import com.nousin.springcloud.common.util.JwtTokenUtil;
import com.nousin.springcloud.common.util.ResultUtil;
import com.nousin.springcloud.server.storage.framework.security.dao.PermissionMapper;
import com.nousin.springcloud.server.storage.framework.security.model.Permission;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * TODO
 *
 * @author Nousin
 * @since 2019/12/24
 */
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.DEFAULT_FILTER_ORDER - 1)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    PermissionMapper permissionMapper;
//    @Autowired
//    TokenAuthenticationFilter tokenAuthenticationFilter;
//    @Autowired
//    MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource;
//    @Autowired
//    MyAccessDecisionManager myAccessDecisionManager;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated()
                .and().addFilterBefore(getOncePerRequestFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedHandler((request, response, e) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json;charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.write(JSONObject.toJSONString(ResultUtil.fail("权限不足，请联系管理员")));
                    out.flush();
                    out.close();
                })
//                .and()
//                .exceptionHandling()
                .authenticationEntryPoint((request, response, e) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json;charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.write(JSONObject.toJSONString(ResultUtil.fail("权限不足，请联系管理员")));
                    out.flush();
                    out.close();
                });
    }


    @Bean
    public OncePerRequestFilter getOncePerRequestFilter() {
        return new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
                UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            }

            private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
                String userInfo = request.getHeader("extractTokenInfo");
                // 如果用户信息不为空则信任该请求
                if (StringUtils.isNotBlank(userInfo)) {
                    return getAuth(userInfo);
                }
                String token = request.getHeader("Authorization");
                if (StringUtils.isBlank(token)) {
                    throw new AuthenticationServiceException("Token is missing.");
                }
                //有token
                try {
                    String fixStart = "bearer ";
                    if (token.toLowerCase().startsWith(fixStart)) {
                        token = token.substring(fixStart.length());
                    }
                    //解密token
                    String extractToken = JwtTokenUtil.extractTokenWithSignKey(token, "");
                    return getAuth(extractToken);
                } catch (ExpiredJwtException e) {
                    log.error("e", e);
                    throw new CredentialsExpiredException("The token is time out.");
                } catch (Exception e) {
                    log.error("e", e);
                    throw new AuthenticationServiceException("Authentication failed.");
                }
            }

            private UsernamePasswordAuthenticationToken getAuth(String tokenInfo) {
                OauthUserInfoDto userInfoDto = JSONObject.parseObject(tokenInfo, OauthUserInfoDto.class);
                // 根据UserInfo 信息查询用户权限
                List<Permission> userPermissions = permissionMapper.findUserPermissions(userInfoDto.getName());
                return new UsernamePasswordAuthenticationToken(userInfoDto, null, userPermissions);
            }
        };
    }
}