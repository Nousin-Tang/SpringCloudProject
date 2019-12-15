package com.vanew.springcloud.newretailing.gateway.framework.security.handler;

//import com.alibaba.fastjson.JSON;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.vanew.springcloud.newretailing.gateway.framework.common.util.ResultUtil;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.server.WebFilterExchange;
//import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;

/**
 * TODO
 *
 * @author tangwc
 * @since 2019/12/13
 */
@Component
public class AuthenticationSuccessHandler
//        extends WebFilterChainServerAuthenticationSuccessHandler
{
//    @Override
//    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
//        ServerWebExchange exchange = webFilterExchange.getExchange();
//        ServerHttpResponse response = exchange.getResponse();
//        //设置headers
//        HttpHeaders httpHeaders = response.getHeaders();
//        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
//        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
//
//        byte[] dataBytes = {};
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            dataBytes = JSON.toJSONBytes(ResultUtil.success(authentication.getPrincipal()));
//        } catch (Exception ex) {
//            dataBytes = JSON.toJSONBytes(ResultUtil.error(""));
//        }
//        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
//        return response.writeWith(Mono.just(bodyDataBuffer));
//    }


//    private AuthUserDetails buildUser(User user) {
//        AuthUserDetails userDetails = new AuthUserDetails();
//        userDetails.setUsername(user.getUsername());
//        userDetails.setPassword(user.getPassword().substring(user.getPassword().lastIndexOf("}") + 1, user.getPassword().length()));
//        return userDetails;
//    }
}
