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
    // 存在则使用配置的key，不存在则使用随机的一个UUID
    @Value("${nousin.jwt.sign-key:123456}")
    private String jwtSignKey; // jwt签名 与Auth-server 系统签名一样

    @Bean("oauthAuthenticationWebFilter")
    AuthenticationWebFilter oauthAuthenticationWebFilter() {

        AuthenticationWebFilter filter = new AuthenticationWebFilter(reactiveAuthenticationManager());
        filter.setServerAuthenticationConverter(oAuthTokenConverter()::apply);
        filter.setAuthenticationSuccessHandler((webFilterExchange, authentication)->{
            ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
            response.setStatusCode(HttpStatus.OK);
            String body = JSONObject.toJSONString(ResultUtil.success(authentication));
            DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        });
        filter.setAuthenticationFailureHandler((webFilterExchange, authenticationException)->{
            ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            String body = JSONObject.toJSONString(ResultUtil.success(authenticationException));
            DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        });
        return filter;
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(){
        final AuthenticationManager authenticationManager;
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


//    @Component
//    public static class OAuth2AuthenticationManagerAdapter implements ReactiveAuthenticationManager {
//
//        private final AuthenticationManager authenticationManager;
//
//        @Autowired
//        public OAuth2AuthenticationManagerAdapter(ResourceServerTokenServices tokenServices) {
//            this.authenticationManager = oauthManager(tokenServices);
//        }
//
//        public Mono<Authentication> authenticate(Authentication token) {
//            return Mono.just(token).publishOn(Schedulers.elastic()).flatMap(t -> {
//                try {
//                    return Mono.just(this.authenticationManager.authenticate(t));
//                } catch (Exception x) {
//                    return Mono.error(new BadCredentialsException("Invalid or expired access token presented"));
//                }
//            }).filter(Authentication::isAuthenticated);
//        }
//
//        private AuthenticationManager oauthManager(ResourceServerTokenServices tokenServices) {
//            OAuth2AuthenticationManager oauthAuthenticationManager = new OAuth2AuthenticationManager();
//
//            oauthAuthenticationManager.setResourceId("");
//            oauthAuthenticationManager.setTokenServices(tokenServices);
//            return oauthAuthenticationManager;
//        }
//
//    }


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
            if (token != null) {
                return Mono.just(new PreAuthenticatedAuthenticationToken(token, ""));
            }
            return Mono.empty();
        };
    }

//    @Component
//    public static class OAuthTokenConverter implements Function<ServerWebExchange, Mono<Authentication>> {
//
//        private static final String BEARER = "bearer ";
//
//        @Override
//        public Mono<Authentication> apply(ServerWebExchange exchange) {
//            String token = extractToken(exchange.getRequest());
//            if (token != null) {
//                return Mono.just(new PreAuthenticatedAuthenticationToken(token, ""));
//            }
//            return Mono.empty();
//        }
//
//        private String extractToken(ServerHttpRequest request) {
//            String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//            if (StringUtils.isBlank(token) || !token.toLowerCase().startsWith(BEARER)) {
//                return null;
//            }
//            return token.substring(BEARER.length());
//        }
//
//    }
}
