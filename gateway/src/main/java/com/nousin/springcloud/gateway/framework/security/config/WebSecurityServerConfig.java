package com.nousin.springcloud.gateway.framework.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

/**
 * 安全配置类
 *
 * @author tangwc
 * @since 2019/12/16
 */
@Configuration
@EnableWebFluxSecurity
public class WebSecurityServerConfig {

    @Bean
    public SecurityWebFilterChain oauthTokenAuthConfig(
            ServerHttpSecurity security, AuthenticationWebFilter oauthAuthenticationWebFilter) {

        return security
                .csrf().disable()
                .logout().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .exceptionHandling().and()
                .securityMatcher(notMatches("/oauth/**"))
                .addFilterAt(oauthAuthenticationWebFilter, SecurityWebFiltersOrder.HTTP_BASIC)
                .authorizeExchange().anyExchange().authenticated()
                .and().build();
    }

    private ServerWebExchangeMatcher matches(String ... routes) {
        return ServerWebExchangeMatchers.pathMatchers(routes);
    }

    private ServerWebExchangeMatcher notMatches(String ... routes) {
        return new NegatedServerWebExchangeMatcher(matches(routes));
    }
}
