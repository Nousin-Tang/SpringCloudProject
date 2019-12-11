package com.nousin.springcloud.server.order.framework.security.converter;

import com.nousin.springcloud.server.order.framework.common.dto.MyUserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * TODO
 *
 * @author tangwc
 * @since 2019/12/12
 */
@Component
public class MyJwtAccessTokenConverter extends JwtAccessTokenConverter {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (accessToken instanceof DefaultOAuth2AccessToken) {
            //((DefaultOAuth2AccessToken) accessToken).setRefreshToken();
            Object principal = authentication.getPrincipal();
            if (principal instanceof MyUserDetails) {
                MyUserDetails myUserDetails = (MyUserDetails) principal;
                HashMap<String, Object> map = new HashMap<>();
                map.put("user_id", myUserDetails.getUser().getId());
                map.put("user_name", myUserDetails.getUser().getUsername());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(map);
            }
        }
        return super.enhance(accessToken, authentication);
    }
}
