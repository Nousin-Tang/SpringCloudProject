package com.nousin.springcloud.auth.framework.security.entity;

import com.nousin.springcloud.common.dto.OauthUserInfoDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * UserDetails 实现
 *
 * @author Nousin
 * @since 2019/12/11
 */
@Getter
@Setter
public class UserDetail extends OauthUserInfoDto implements UserDetails, CredentialsContainer {

    private String username; // 用户登录名称
    private String password; // 密码
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public void eraseCredentials() {
        authorities = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
