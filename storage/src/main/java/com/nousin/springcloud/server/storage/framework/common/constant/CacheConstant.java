package com.nousin.springcloud.server.storage.framework.common.constant;

/**
 * 缓存相关常量类
 * 
 * @author zhyj
 * @version 2019-03-01
 *
 */
public final class CacheConstant {
	public static final String system_mark = "agent_";

	//缓存key-erpweb字典表
	public static final String SYS_CACHE_DICT_MAP = system_mark + "sys_dict_map";

	// 用户信息
	public static final String SYS_CACHE_USER = system_mark + "sys_user_map";

	//用户ID 前缀 id_
	public static final String SYS_CACHE_USER_ID = system_mark + "id_";

	//用户ID 前缀 login_name_
	public static final String SYS_CACHE_CUSTOMERS_LOGIN_NAME = system_mark + "login_name";

	//用户权限
	public static final String SYS_CACHE_MENU = system_mark + "sys_menu_map";

	//用户ID 前缀 menuList_
	public static final String SYS_CACHE_MENU_PRE = system_mark + "menuList_";

}
