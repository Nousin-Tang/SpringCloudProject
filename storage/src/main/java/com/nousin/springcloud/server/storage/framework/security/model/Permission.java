package com.nousin.springcloud.server.storage.framework.security.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

/**
 * 权限实体类
 *
 * @author Nousin
 * @since 2019/12/24
 */
@Getter
@Setter
public class Permission implements GrantedAuthority {
    private String id;
    private String url;
    private String permission;

    @Override
    public String getAuthority() {
        return permission;
    }
}
