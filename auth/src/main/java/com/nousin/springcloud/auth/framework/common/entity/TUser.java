package com.nousin.springcloud.auth.framework.common.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * TODO
 *
 * @author tangwc
 * @since 2019/12/11
 */
@Getter
@Setter
public class TUser implements UserDetails, CredentialsContainer {
    private String id; // 用户Id
    private String username; // 用户名称
    private String password; // 密码
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public void eraseCredentials() {
        this.authorities = null;
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
