package com.nousin.springcloud.server.storage.framework.security.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nousin.springcloud.common.dto.OauthUserInfoDto;
import com.nousin.springcloud.common.util.JwtTokenUtil;
import com.nousin.springcloud.server.storage.framework.common.util.UserContextUtil;
import com.nousin.springcloud.server.storage.framework.security.dao.PermissionMapper;
import com.nousin.springcloud.server.storage.framework.security.model.Permission;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * 服务层 Spring Security 配置
 *
 * @author Nousin
 * @since 2019/12/24
 */
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.DEFAULT_FILTER_ORDER - 1)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${nousin.jwt.signKey:123456}")
	private String jwtSignKey; // jwt签名 与Auth-server 系统签名一样

	@Value("${nousin.security.enable:true}")
	private Boolean securityEnable; // 是否进行权限校验

	@Value("${nousin.swagger.enable:false}")
	private Boolean swaggerEnable; // 是否进行权限校验
	@Value("${nousin.language:en_US}")
	private String language; // 是否进行权限校验

	@Autowired
	PermissionMapper permissionMapper;

	@Autowired
	private MyAuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	private MyAccessDeniedHandler accessDeniedHandler;

//	@Autowired
//	LocaleResolver localeResolver;

	private static String[] ignoreUrls = {};
	private static String[] ignoreSwaggerUrls = { "/swagger-ui.html", "/swagger-resources/**", "/images/**",
			"/webjars/**", "/v2/api-docs", "/configuration/ui", "/configuration/security" };

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
				.headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
				new Header("Access-control-Allow-Origin", "*"),
				new Header("Access-control-Allow-Headers", "*"),
				new Header("Access-Control-Expose-Headers", "Content-Disposition"))));
		http.authorizeRequests().antMatchers(ignoreUrls).permitAll();
		if (swaggerEnable) {
			http.authorizeRequests().antMatchers(ignoreSwaggerUrls).permitAll();
		}
		if (!securityEnable)
			http.authorizeRequests().anyRequest().permitAll();
		else
			http.authorizeRequests().anyRequest().authenticated().and()
					.addFilterBefore(getOncePerRequestFilter(), UsernamePasswordAuthenticationFilter.class)
					.exceptionHandling()
					// 权限列表没有该权限时，调用此方法
					.accessDeniedHandler(accessDeniedHandler)
					// 校验出现异常或者权限列表为空时，调用此方法
					.authenticationEntryPoint(authenticationEntryPoint);
	}

	@Bean
	public OncePerRequestFilter getOncePerRequestFilter() {
		List<String> delimiters = Arrays.asList("-", "_");
		return new OncePerRequestFilter() {

			@Override
			public boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
				// 设置语言环境
				setLocale(request.getHeader("lang"));

				// 未开启权限校验
//				if (!securityEnable) {
//					return true;
//				}
				// 开启Swagger
				if (swaggerEnable && ignoreSwaggerUrls.length > 0) {
					boolean skipSwagger = urlMatched(request.getRequestURI(), ignoreSwaggerUrls);
					if (skipSwagger)
						return true;
				}
				// 忽略验证的请求
				if (ignoreUrls.length > 0) {
					boolean skipIgnore = urlMatched(request.getRequestURI(), ignoreUrls);
					if (skipIgnore)
						return true;
				}
				return false;
			}

			public boolean urlMatched(String requestURI, String[] ignoreUrls) {
				if (ignoreUrls.length == 0) {
					return false;
				}
				AntPathMatcher antPathMatcher = new AntPathMatcher();
				for (String pattern : ignoreUrls) {
					if (antPathMatcher.match(pattern, requestURI))
						return true;
				}
				return false;
			}

			public void setLocale(String lang){
				if(StringUtils.isBlank(lang)){
					lang = language;
				}
				for (String delimiter : delimiters) {
					if(lang.indexOf(delimiter)>0){
						String[] split = lang.split(delimiter);
						LocaleContextHolder.setLocale(new Locale(split[0],split[1]));
						// 在  com.nousin.springcloud.server.storage.framework.config.WebMvcConfig.localeResolver 中起作用
						Locale.setDefault(new Locale(split[0],split[1]));
						break;
					}
				}
			}

			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
					throws IOException, ServletException {
				String userInfo = request.getHeader("extractTokenInfo");

				// 如果用户信息不为空则信任该请求
				if (StringUtils.isNotBlank(userInfo)) {
					SecurityContextHolder.getContext().setAuthentication(getAuth(userInfo));
					chain.doFilter(request, response);
					return;
				}

				if (!securityEnable) {
					SecurityContextHolder.getContext().setAuthentication(getAuth(JSON.toJSONString(UserContextUtil.getUser())));
					chain.doFilter(request, response);
					return;
				}

				String token = request.getHeader("Authorization");
				if (StringUtils.isBlank(token)) {
					authenticationEntryPoint.commence(request, response,
							new AuthenticationServiceException("Token is missing."));
					return;
				}
				//有token
				try {
					String fixStart = "bearer ";
					if (token.toLowerCase().startsWith(fixStart)) {
						token = token.substring(fixStart.length());
					}
					//解密token
					String extractToken = JwtTokenUtil.extractTokenWithSignKey(token, jwtSignKey);
					SecurityContextHolder.getContext().setAuthentication(getAuth(extractToken));
				} catch (ExpiredJwtException e) {
					SecurityContextHolder.clearContext();
					authenticationEntryPoint.commence(request, response,
							new CredentialsExpiredException("The token is time out."));
					return;
				} catch (Exception e) {
					SecurityContextHolder.clearContext();
					authenticationEntryPoint.commence(request, response,
							new AuthenticationServiceException("Authentication failed."));
					return;
				}
				chain.doFilter(request, response);
			}

			private UsernamePasswordAuthenticationToken getAuth(String tokenInfo) {
				OauthUserInfoDto userInfoDto = JSONObject.parseObject(tokenInfo, OauthUserInfoDto.class);
				// 根据UserInfo 信息查询用户权限
				List<Permission> userPermissions = new ArrayList<>();
				if (securityEnable) {
					userPermissions.addAll(permissionMapper.listPermissionsById(userInfoDto.getId()));
				} else {
					userPermissions.addAll(permissionMapper.listPermissions());
				}
				// 如果没有权限则 验证失败
				if (userPermissions.size() == 0) {
					throw new RuntimeException("No permission data");
				}
				return new UsernamePasswordAuthenticationToken(userInfoDto, null, userPermissions);
			}

		};
	}
}