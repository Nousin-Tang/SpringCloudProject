package com.nousin.springcloud.server.storage.framework.security.dao;

import com.nousin.springcloud.server.storage.framework.security.model.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TODO
 *
 * @author Nousin
 * @since 2019/12/24
 */
@Repository
public interface PermissionMapper {
    List<Permission> findAllPermissions();
}
