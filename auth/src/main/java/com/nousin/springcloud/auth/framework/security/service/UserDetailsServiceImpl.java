package com.nousin.springcloud.auth.framework.security.service;

import com.nousin.springcloud.auth.framework.security.entity.UserDetail;
import com.nousin.springcloud.common.util.PasswordUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 实现 UserDetailsService 接口的用户Service
 *
 * @author tangwc
 * @since 2019/12/11
 */
@Service("userDetailService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        // userDao.getUserByUsername(username);

		UserDetail userInfo = new UserDetail();
		userInfo.setId("1");
		userInfo.setUsername(username);
		userInfo.setPassword(PasswordUtil.encode("123456"));
		userInfo.setName(username);
		userInfo.setLanguage("zh-en");
		userInfo.setPlatform("partner");
		if (StringUtils.endsWithIgnoreCase("partner", "partner")) {
			userInfo.setPartnerCode("KD");
		} else {
			userInfo.setTenantCode("HD");
		}
        userInfo.setAuthorities(AuthorityUtils.createAuthorityList("user"));
        return userInfo;
    }
}
