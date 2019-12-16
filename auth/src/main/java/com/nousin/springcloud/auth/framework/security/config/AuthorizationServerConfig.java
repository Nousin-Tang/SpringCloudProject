package com.nousin.springcloud.auth.framework.security.config;

import com.nousin.springcloud.auth.framework.security.entity.UserDetail;
import com.nousin.springcloud.common.dto.OauthUserInfoDto;
import com.nousin.springcloud.common.util.DozerUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 授权服务器配置
 *
 * @author tangwc
 * @since 2019/12/10
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    // 签名
    @Value("${nousin.jwt.sign-key:123456}")
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





    private AuthenticationManager authenticationManager;
//    private PasswordEncoder passwordEncoder;
//    private DataSource dataSource;

    /**
     * 构造器注入属性
     *
     * @param authenticationManager 认证管理
     *                              //     * @param passwordEncoder       密码加密方式
     *                              //     * @param dataSource            数据源
     */
    @Autowired
    public AuthorizationServerConfig(AuthenticationManager authenticationManager
//            , PasswordEncoder passwordEncoder
//            , DataSource dataSource
    ) {
        this.authenticationManager = authenticationManager;
//        this.passwordEncoder = passwordEncoder;
//        this.dataSource = dataSource;
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
        endpoints
                // .pathMapping("/oauth/token","/login")// 令牌端点 路由 映射到自定义 /login 路由
                .tokenStore(tokenStore())
                .accessTokenConverter(jwtAccessTokenConverter())
                .authenticationManager(authenticationManager)
                .tokenEnhancer(enhancerChain);
//        endpoints.tokenServices(defaultTokenServices());
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
                .secret(clientSecret)
                .authorizedGrantTypes(grantType.split(","))
                .scopes(scope)
                .accessTokenValiditySeconds(jwtValiditySeconds);
//        clients.jdbc(dataSource).passwordEncoder(passwordEncoder);
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
            additionalInfo.put("extra", userDto);
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

//    /**
//     * <p>注意，自定义TokenServices的时候，需要设置@Primary，否则报错，</p>
//     * @return
//     */
//    @Primary
//    @Bean
//    public DefaultTokenServices defaultTokenServices(){
//        DefaultTokenServices tokenServices = new DefaultTokenServices();
//        tokenServices.setTokenStore(tokenStore());
//        tokenServices.setSupportRefreshToken(true);
//        //tokenServices.setClientDetailsService(clientDetails());
//        // token有效期自定义设置，默认12小时
//        tokenServices.setAccessTokenValiditySeconds(60*60*12);
//        // refresh_token默认30天
//        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);
//        return tokenServices;
//    }

//     基于Redis的存储方式
//     redis连接工厂
//    @Autowired
//    private JedisConnectionFactory JedisConnectionFactory;
//    /**
//     * 令牌存储
//     * @return redis令牌存储对象
//     */
//    @Bean
//    public TokenStore tokenStore() {
//        return new RedisTokenStore(JedisConnectionFactory);
//    }
}
