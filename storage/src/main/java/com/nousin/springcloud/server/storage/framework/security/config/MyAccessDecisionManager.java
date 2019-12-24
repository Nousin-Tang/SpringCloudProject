package com.nousin.springcloud.server.storage.framework.security.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.nousin.springcloud.common.dto.OauthUserInfoDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;

/**
 * TODO
 *
 * @author Nousin
 * @since 2019/12/24
 */
@Service
@Slf4j
public class MyAccessDecisionManager implements AccessDecisionManager {
    // 存在则使用配置的key，不存在则使用随机的一个UUID
    @Value("${nousin.jwt.sign-key:123456}")
    private String jwtSignKey; // jwt签名 与Auth-server 系统签名一样
    /**
     * 判断用户是否有权限访问
     * @param authentication 用户具有的权限集合
     * @param object 包含客户端发起的请求的requset信息
     * @param configAttributes MyFilterInvocationSecurityMetadataSource的getAttributes(Object object) 返回的集合
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        String userInfo = request.getHeader("extractTokenInfo");
        // 如果用户信息不为空则信任该请求
        if(StringUtils.isNotBlank(userInfo)){
            OauthUserInfoDto userInfoDto = JSONObject.parseObject(userInfo, OauthUserInfoDto.class);
        }
        String token = request.getHeader("Authorization");
        //有token
        try {
            String fixStart = "bearer ";
            if (token.toLowerCase().startsWith(fixStart)) {
                token = token.substring(fixStart.length());
            }
            //解密token
            String extractToken = extractTokenWithSignKey(token);
            OauthUserInfoDto userInfoDto = JSONObject.parseObject(extractToken, OauthUserInfoDto.class);
            //
            // 构造新的请求

            return;
        } catch (ExpiredJwtException e) {
            log.error("e", e);
            throw new CredentialsExpiredException("The token is time out.");
        } catch (Exception e) {
            log.error("e", e);
            throw new AuthenticationServiceException("Authentication failed.");
        }
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
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
