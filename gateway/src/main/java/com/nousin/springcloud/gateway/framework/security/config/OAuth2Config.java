package com.nousin.springcloud.gateway.framework.security.config;

import com.alibaba.fastjson.JSONObject;
import com.nousin.springcloud.common.util.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * TODO
 *
 * @author tangwc
 * @since 2019/12/16
 */
@Configuration
public class OAuth2Config {
    // jwt签名与Auth-server系统签名一样
    @Value("${nousin.jwt.sign-key:123456}")
    private String jwtSignKey;

    @Bean("oauthAuthenticationWebFilter")
    AuthenticationWebFilter oauthAuthenticationWebFilter() {

        AuthenticationWebFilter filter = new AuthenticationWebFilter(reactiveAuthenticationManager());
        filter.setServerAuthenticationConverter(oAuthTokenConverter()::apply);
        filter.setAuthenticationSuccessHandler((exchange, authentication)->{
            ServerWebExchange serverWebExchange = exchange.getExchange();
            serverWebExchange.getResponse().getHeaders().add("userInfo", authentication.toString());
            return exchange.getChain().filter(serverWebExchange);
        });
        filter.setAuthenticationFailureHandler((webFilterExchange, authenticationException)->{
            ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            String body = JSONObject.toJSONString(ResultUtil.success(authenticationException.getMessage()));
            DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        });
        return filter;
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(){
        return (authentication)->
            Mono.just(authentication).publishOn(Schedulers.elastic()).flatMap(t -> {
                try {
                    return Mono.just(oauthManager().authenticate(t));
                } catch (Exception x) {
                    return Mono.error(new BadCredentialsException("Invalid or expired access token presented"));
                }
            }).filter(Authentication::isAuthenticated);
    }
    private AuthenticationManager oauthManager() {
        OAuth2AuthenticationManager oauthAuthenticationManager = new OAuth2AuthenticationManager();
        oauthAuthenticationManager.setResourceId("");
        oauthAuthenticationManager.setTokenServices(tokenServices());
        return oauthAuthenticationManager;
    }

    @Bean
    public ResourceServerTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setSupportRefreshToken(false);
        tokenServices.setTokenStore(tokenStore());
        return tokenServices;
    }

    /**
     * Token 存储方式
     *
     * @return
     */
    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * Token转换类
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        // 对称加密
        accessTokenConverter.setSigningKey(jwtSignKey);
        return accessTokenConverter;
    }

    public Function<ServerWebExchange, Mono<Authentication>> oAuthTokenConverter(){
        final String BEARER = "bearer ";
        return (exchange)-> {
            String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (StringUtils.isBlank(authorization) || !authorization.toLowerCase().startsWith(BEARER)) {
                return Mono.empty();
            }
            String token = authorization.substring(BEARER.length());
            if (StringUtils.isNotBlank(token)) {
                return Mono.just(new PreAuthenticatedAuthenticationToken(token, ""));
            }
            return Mono.empty();
        };
    }
}
