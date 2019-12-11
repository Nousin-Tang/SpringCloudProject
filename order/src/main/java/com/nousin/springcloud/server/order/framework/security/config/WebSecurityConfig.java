package com.nousin.springcloud.server.order.framework.security.config;


import com.nousin.springcloud.server.order.framework.security.handler.LoginFailureHandler;
import com.nousin.springcloud.server.order.framework.security.handler.LoginSuccessHandler;
import com.nousin.springcloud.server.order.framework.security.service.UserDetailsServiceImpl;
import com.nousin.springcloud.server.order.framework.security.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * WEB安全配置类
 * 用于保护oauth要开放的资源，同时主要作用于client端以及token的认证(Bearer auth)
 *
 * @author tangwc
 * @since 2019/12/10
 */
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(2)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userService;
    private LoginSuccessHandler loginSuccessHandler;
    private LoginFailureHandler loginFailureHandler;

    /**
     * 构造器注入用户Service
     * 推荐使用此方法注入属性
     *
     * @param userService 用户Service
     */
    @Autowired
    public WebSecurityConfig(@Qualifier("userDetailService") UserDetailsServiceImpl userService,
                             @Qualifier("loginSuccessHandler") LoginSuccessHandler loginSuccessHandler,
                             @Qualifier("loginFailureHandler") LoginFailureHandler loginFailureHandler
    ) {
        this.userService = userService;
        this.loginSuccessHandler = loginSuccessHandler;
        this.loginFailureHandler = loginFailureHandler;
    }

    /**
     * 全局用户信息
     *
     * @param auth 认证管理
     * @throws Exception 用户认证异常信息
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    /**
     * http安全配置
     *
     * @param http http安全对象
     * @throws Exception http安全异常信息
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS).permitAll()
//                .antMatchers("/oauth/**").permitAll()
//                .antMatchers("/login/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().loginPage("/login").successHandler(loginSuccessHandler).failureHandler(loginFailureHandler).permitAll()
//                .and()
//                .httpBasic()
//                ;
//                .and()
//                .csrf().disable();

        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .and()
                .requestMatchers()
                .antMatchers("/oauth/**")
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .and()
                .csrf().disable();
    }

    /**
     * 认证管理
     *
     * @return 认证管理对象
     * @throws Exception 认证异常信息
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * 密码加密方式
     *
     * @return 密码加密对象
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();

        // 使用jeeSite内部加密算法
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return PasswordUtil.encode(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return PasswordUtil.matches(rawPassword, encodedPassword);
            }
        };
    }
}
