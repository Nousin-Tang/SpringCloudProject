package com.nousin.springcloud.gateway.framework.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.nousin.springcloud.common.util.ResultUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JwtToken 过滤器
 *
 * @author tangwc
 * @since 2019/12/10
 */
@Component
@Order(-201)
@Slf4j
public class JwtTokenFilter implements GlobalFilter {
	// 存在则使用配置的key，不存在则使用随机的一个UUID
	@Value("${nousin.jwt.sign-key:123456}")
	private String jwtSignKey; // jwt签名 与Auth-server 系统签名一样

	/**
	 * 授权访问用户名
	 */
	@Value("${spring.security.user.name:client}")
	private String securityUserName;
	/**
	 * 授权访问密码
	 */
	@Value("${spring.security.user.password:secret}")
	private String securityUserPassword;

	/**
	 * 过滤器
	 *
	 * @param exchange
	 * @param chain
	 * @return
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String url = request.getURI().getPath();
		log.info(url);
		//跳过不需要验证的路径
		AntPathMatcher authMatcher = new AntPathMatcher();
		if (authMatcher.match("/oauth/**", url)) {
			// oauth授权
			String auth = securityUserName.concat(":").concat(securityUserPassword);
			String encodedAuth = new sun.misc.BASE64Encoder().encode(auth.getBytes(StandardCharsets.UTF_8));
			//向headers中放授权信息 注意Basic后面有空格
			ServerHttpRequest serverHttpRequest = request.mutate().header("Authorization", "Basic " + encodedAuth).build();
			//将现在的request变成change对象
			ServerWebExchange build = exchange.mutate().request(serverHttpRequest).build();
			return chain.filter(build);
		}
		HttpHeaders headers = request.getHeaders();
		//获取token
		String token = headers.getFirst("Authorization");
		if (StringUtils.isBlank(token)) {
			//没有token
			return returnAuthFail(exchange, "You do not have the Authorization. Please sign in first.");
		} else {
			//有token
			try {
				String fixStart = "bearer ";
				if (token.toLowerCase().startsWith(fixStart)) {
					token = token.substring(fixStart.length());
				}
				//解密token
				String extractToken = extractToken(token);
				// 构造新的请求
				ServerHttpRequest serverHttpRequest = request.mutate().header("tokenInfo", extractToken).build();
				return chain.filter(exchange.mutate().request(serverHttpRequest).build());
			} catch (ExpiredJwtException e) {
				log.error("e", e);
				return returnAuthFail(exchange, "The token is time out.");
			} catch (Exception e) {
				log.error("e", e);
				return returnAuthFail(exchange, "Token verification failed.");
			}
		}
	}

	/**
	 * 返回校验失败
	 *
	 * @param exchange
	 * @return
	 */
	private Mono<Void> returnAuthFail(ServerWebExchange exchange, String message) {
		ServerHttpResponse serverHttpResponse = exchange.getResponse();
		serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
		DataBuffer buffer = serverHttpResponse.bufferFactory().wrap(JSON.toJSONBytes(ResultUtil.error(message)));
		return serverHttpResponse.writeWith(Flux.just(buffer));
	}

	/**
	 * 获取token信息
	 *
	 * @param token
	 * @return
	 */
	private String extractToken(String token) {
		Jwt decode = JwtHelper.decode(token);
		JSONObject jsonObject = JSON.parseObject(decode.getClaims());
		Object eval = JSONPath.eval(jsonObject, "$.extra");
		if (eval == null || StringUtils.isBlank(eval.toString())) {
			throw new RuntimeException();
		}
		return eval.toString();
	}
	private String extractTokenWithSignKey(String token) {
		Jws<Claims> claimsJws = Jwts.parser().setSigningKey(jwtSignKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token);
		Date expiration = claimsJws.getBody().getExpiration();
		JSONObject jsonObject = JSON.parseObject(claimsJws.getBody().toString());
		Object eval = JSONPath.eval(jsonObject, "$.extra");
		if (eval == null || StringUtils.isBlank(eval.toString())) {
			throw new RuntimeException();
		}
		return claimsJws.toString();
	}
}
