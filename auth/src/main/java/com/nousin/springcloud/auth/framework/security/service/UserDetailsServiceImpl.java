package com.nousin.springcloud.auth.framework.security.service;

import com.nousin.springcloud.auth.framework.common.dto.MyUserDetails;
import com.nousin.springcloud.auth.framework.common.entity.TUser;
import com.nousin.springcloud.auth.framework.security.util.PasswordUtil;
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
        TUser userInfo = new TUser(); // userDao.getUserByUsername(username);
        userInfo.setId("1");
        userInfo.setUsername("admin");
        userInfo.setPassword(PasswordUtil.encode("123456"));
        userInfo.setAuthorities(AuthorityUtils.createAuthorityList("admin","user"));
        //需要构造org.springframework.security.core.userdetails.User 对象包含账号密码还有用户的角色
        return new MyUserDetails(userInfo);
    }
}
