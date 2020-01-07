package com.nousin.springcloud.server.storage.framework.common.util;

import com.nousin.springcloud.common.dto.OauthUserInfoDto;
import com.nousin.springcloud.common.util.DozerUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;

/**
 * Spring Security 工具类 获取用户信息
 *
 * @author Nousin
 * @since 2020/1/6
 */
public class SecurityContextUtil {

	/**
	 * 获取用户信息
	 * @return
	 */
	public static OauthUserInfoDto getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getPrincipal() == null) {
			OauthUserInfoDto defaultUserInfo = new OauthUserInfoDto();
			defaultUserInfo.setId("1");
			defaultUserInfo.setPartnerCode("KD");
			defaultUserInfo.setPlatform("partner");
			defaultUserInfo.setLanguage("en_US");
			return defaultUserInfo;
		}
		return DozerUtil.map(authentication.getPrincipal(), OauthUserInfoDto.class);
	}

	/**
	 * 获取用户权限信息
	 * @return
	 */
	public static ArrayList<GrantedAuthority> getUserAuthorities() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getPrincipal() == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(authentication.getAuthorities());
	}
}
