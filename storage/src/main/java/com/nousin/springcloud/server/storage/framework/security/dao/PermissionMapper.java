package com.nousin.springcloud.server.storage.framework.security.dao;

import com.nousin.springcloud.server.storage.framework.security.model.Permission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 获取权限接口
 *
 * @author Nousin
 * @since 2019/12/24
 */
@Repository
public interface PermissionMapper {

    @Select("SELECT DISTINCT IFNULL(c.permission,'') as permission " +
            "FROM sys_user u " +
            "INNER JOIN sys_user_role a on a.user_id = u.id " +
            "INNER JOIN sys_role_menu b ON b.role_id = a.role_id " +
            "INNER JOIN sys_menu c ON c.id = b.menu_id AND c.del_flag = 0 " +
            "where u.id=#{userId} and u.del_flag=0 and u.login_flag=1 " +
            "and c.permission is not NULL and c.permission <> '' ")
    List<Permission> listPermissionsById(@Param("userId") String userId);

    @Select("SELECT DISTINCT IFNULL(c.permission,'') as permission " +
            "FROM sys_user u " +
            "INNER JOIN sys_user_role a on a.user_id = u.id " +
            "INNER JOIN sys_role_menu b ON b.role_id = a.role_id " +
            "INNER JOIN sys_menu c ON c.id = b.menu_id AND c.del_flag = 0 " +
            "where u.login_name=#{loginName} and u.del_flag=0 and u.login_flag=1 " +
            "and c.permission is not NULL and c.permission <> ''")
    List<Permission> listPermissionsByName(@Param("login_name") String loginName);

    @Select("SELECT DISTINCT c.permission as permission FROM sys_menu c " +
            "where c.del_flag = 0 and c.permission is not NULL and c.permission <> ''")
    List<Permission> listPermissions();
}
