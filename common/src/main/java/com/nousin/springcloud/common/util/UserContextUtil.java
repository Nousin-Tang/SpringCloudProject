package com.nousin.springcloud.common.util;

import com.nousin.springcloud.common.dto.OauthUserInfoDto;

import java.util.Optional;

/**
 * 用户上下文工具类 （需要主动释放资源）
 *
 * @author Nousin
 * @since 2019/12/16
 */
@Deprecated
public class UserContextUtil {
	private ThreadLocal<OauthUserInfoDto> threadLocal;

	private UserContextUtil() {
		this.threadLocal = new ThreadLocal<OauthUserInfoDto>();
	}

	/**
	 * 创建实例
	 *
	 * @return
	 */
	public static UserContextUtil getInstance() {
		return SingletonHolder.sInstance;
	}

	/**
	 * 静态内部类单例模式
	 * 单例初使化
	 */
	private static class SingletonHolder {
		private static final UserContextUtil sInstance = new UserContextUtil();
	}

	/**
	 * 用户上下文中放入信息
	 *
	 * @param dto
	 */
	public void setContext(OauthUserInfoDto dto) {
		threadLocal.set(dto);
	}

	/**
	 * 获取上下文中的信息
	 *
	 * @return 上下文信息
	 */
	public OauthUserInfoDto getContext() {
		return threadLocal.get();
	}

	/**
	 * 清空上下文
	 */
	public void clear() {
		threadLocal.remove();
	}

	/**
	 * 获取上下文中的用户名
	 *
	 * @return 用户名称
	 */
	public String getUsername() {
		return Optional.ofNullable(threadLocal.get()).orElse(new OauthUserInfoDto()).getName();
	}
}
