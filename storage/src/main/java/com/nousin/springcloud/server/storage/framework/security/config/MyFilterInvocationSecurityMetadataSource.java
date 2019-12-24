package com.nousin.springcloud.server.storage.framework.security.config;

import com.nousin.springcloud.server.storage.framework.security.dao.PermissionMapper;
import com.nousin.springcloud.server.storage.framework.security.model.Permission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * TODO
 *
 * @author Nousin
 * @since 2019/12/24
 */
@Service
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    PermissionMapper permissionMapper;

    private HashMap<String, Collection<ConfigAttribute>> map = null;


    /**
     * 判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法， 用来判定用户是否有此权限。如果不在权限表中则放行。
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if (map == null) {
            loadResourceDefine();
        }
        // object 中包含用户请求的request的信息
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        for (Map.Entry<String, Collection<ConfigAttribute>> entry : map.entrySet()) {
            String url = entry.getKey();
            if (new AntPathRequestMatcher(url).matches(request)) {
                return map.get(url);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    /**
     * 加载权限表中所有权限
     */
    public void loadResourceDefine() {
        map = new HashMap<>();
        List<Permission> permissions = permissionMapper.findAllPermissions();
        for (Permission permission : permissions) {
            if(StringUtils.isNotBlank(permission.getUrl()) && StringUtils.isNotBlank(permission.getPermission())){
                ConfigAttribute cfg = new SecurityConfig(permission.getPermission());
                List<ConfigAttribute> list = new ArrayList<>();
                list.add(cfg);
                map.put(permission.getUrl(), list);
            }
        }

    }
}
