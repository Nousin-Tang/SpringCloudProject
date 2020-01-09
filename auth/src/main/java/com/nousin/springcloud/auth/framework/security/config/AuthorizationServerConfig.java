package com.nousin.springcloud.auth.framework.security.config;

import com.nousin.springcloud.auth.framework.security.entity.UserDetail;
import com.nousin.springcloud.auth.framework.security.service.UserDetailsServiceImpl;
import com.nousin.springcloud.common.dto.OauthUserInfoDto;
import com.nousin.springcloud.common.util.DozerUtil;
import com.nousin.springcloud.common.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 授权服务器配置
 *
 * @author Nousin
 * @since 2019/12/10
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    // 签名
    @Value("${nousin.jwt.signKey:123456}")
    private String signKey;

    // 失效时间
    @Value("${nousin.jwt.validity-seconds:7200}")
    private int jwtValiditySeconds;
    // 失效时间
    @Value("${nousin.oauth2-client.clientId:7200}")
    private String clientId;
    @Value("${nousin.oauth2-client.clientSecret:secret}")
    private String clientSecret;
    @Value("${nousin.oauth2-client.grantType:password}")
    private String grantType;
    @Value("${nousin.oauth2-client.scope:all}")
    private String scope;
    @Value("${nousin.principleInfo:principleInfo}")
    private String principleInfo;

    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier("userDetailService")
    private UserDetailsServiceImpl userDetailService;

    /**
     * 构造器注入属性
     *
     * @param authenticationManager 认证管理
     *                              //     * @param passwordEncoder       密码加密方式
     *                              //     * @param dataSource            数据源
     */
    @Autowired
    public AuthorizationServerConfig(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailService) {
        this.authenticationManager = authenticationManager;
        this.userDetailService = userDetailService;
    }

    /**
     * 配置授权服务器端点，如令牌存储，令牌自定义，用户批准和授权类型，不包括端点安全配置
     *
     * @param endpoints 端点
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        //      将增强的token设置到增强链中
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(Arrays.asList(jwtTokenEnhancer(), jwtAccessTokenConverter()));
        endpoints.tokenStore(tokenStore())
                .accessTokenConverter(jwtAccessTokenConverter())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailService)
                .tokenEnhancer(enhancerChain)
        ;
    }

    /**
     * 配置授权服务器端点的安全
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")// 开启/oauth/token_key验证端口无权限访问
                .checkTokenAccess("isAuthenticated()")// 开启/oauth/check_token验证端口认证权限访问
                .allowFormAuthenticationForClients();//主要是让/oauth/token支持client_id以及client_secret作登录认证
    }

    /**
     * 配置 ClientDetailsService 也就是客户端属性信息
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(clientId)
                .secret(PasswordUtil.encode(clientSecret))
                .authorizedGrantTypes(grantType.split(","))
                .scopes(scope)
                .accessTokenValiditySeconds(jwtValiditySeconds);
    }

    /**
     * Token 存储方式
     *
     * @return
     */
//    @Bean
//    public InMemoryTokenStore tokenStore(){
//        return new InMemoryTokenStore();
//    }
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
        accessTokenConverter.setSigningKey(signKey);
        return accessTokenConverter;
    }

    @Bean
    public TokenEnhancer jwtTokenEnhancer() {
        return (accessToken, authentication) -> {
            // 用户信息
            UserDetail userDetail = (UserDetail) authentication.getPrincipal();
            OauthUserInfoDto userDto = DozerUtil.map(userDetail, OauthUserInfoDto.class);
            Map<String, Object> additionalInfo = new HashMap<>();
            additionalInfo.put(principleInfo, userDto);
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }
}
