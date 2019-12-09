package com.nousin.springcloud.gateway.framework.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

/**
 * Jwt 验证过滤器
 *
 * @author tangwc
 * @since 2019/12/9
 */
public class JwtGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    @Override
    public GatewayFilter apply(Object config) {
        return null;
    }
}
