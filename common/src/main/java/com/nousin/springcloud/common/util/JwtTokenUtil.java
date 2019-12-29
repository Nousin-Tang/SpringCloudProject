package com.nousin.springcloud.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtToken 工具类
 *
 * @author Nousin
 * @since 2019/12/27
 */
public class JwtTokenUtil {


    /**
     * 获取token
     * @param object
     * @param signKey
     * @param date
     * @return
     */
    public static String encrypt(Object object, String signKey, Date date) {
        Map<String, Object> claims = new HashMap<>(1);
        claims.put("extra", object);
        return Jwts.builder().addClaims(claims).setExpiration(date).signWith(SignatureAlgorithm.RS256, signKey).compact();
    }

    /**
     * 解析Token
     *
     * @param token
     * @param jwtSignKey
     * @return
     */
    public static String extractTokenWithSignKey(String token, String jwtSignKey) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(jwtSignKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token);
        Date expiration = claimsJws.getBody().getExpiration();
        if (expiration != null && DateUtil.toLocalDateTime(expiration).isBefore(DateUtil.now())) {
            throw new ExpiredJwtException(claimsJws.getHeader(), claimsJws.getBody(), "Token is expired");
        }
        String jsonString = JSON.toJSONString(claimsJws.getBody());
        Object eval = JSONPath.eval(jsonString, "$.extra");
        if (eval == null || StringUtils.isBlank(eval.toString())) {
            throw new RuntimeException();
        }
        return eval.toString();
    }
    public static String extractTokenWithSignKey(String token, String jwtSignKey, String jsonProperties) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(jwtSignKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token);
        Date expiration = claimsJws.getBody().getExpiration();
        if (expiration != null && DateUtil.toLocalDateTime(expiration).isBefore(DateUtil.now())) {
            throw new ExpiredJwtException(claimsJws.getHeader(), claimsJws.getBody(), "Token is expired");
        }
        String jsonString = JSON.toJSONString(claimsJws.getBody());
        if(StringUtils.isNotBlank(jsonProperties)){
            Object eval = JSONPath.eval(jsonString, "$.extra");
            if (eval == null || StringUtils.isBlank(eval.toString())) {
//                throw new RuntimeException();
                return null;
            }
            return eval.toString();
        }
        return jsonString;
    }
}
