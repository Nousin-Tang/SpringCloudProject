package com.vanew.springcloud.newretailing.gateway.framework.security.config;

//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;


/**
 * Spring Security 安全配置类
 *
 * @author tangwc
 * @since 2019/12/10
 */
//@Configuration
//@EnableWebSecurity
//@Order(2)
public class WebSecurityServerConfig
//        extends WebSecurityConfigurerAdapter
{

    /**
     * http安全配置
     *
     * @param http http安全对象
     * @throws Exception http安全异常信息
     */
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//
//        http.authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS).permitAll()
//                .and()
//                // 基于token，所以不需要session
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .csrf().disable();
//        // 禁用缓存
//        http.headers().cacheControl();
//    }

}
