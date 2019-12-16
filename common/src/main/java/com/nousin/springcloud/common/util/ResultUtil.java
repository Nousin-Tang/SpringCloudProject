package com.nousin.springcloud.common.util;

import com.nousin.springcloud.common.dto.ResultDto;
import com.sun.istack.internal.Nullable;

/**
 * 结果返回工具类
 *
 * @author tangwc
 * @since 2019/12/16
 */
public class ResultUtil {
	/**
	 * 操作成功
	 * @param data
	 * @return
	 */
	public static ResultDto success(Object data) {
		return new ResultDto("0", "操作成功", data);
	}

	public static ResultDto success(String message, Object data) {
		return new ResultDto("0", message, data);
	}

	/**
	 * 系统错误
	 * @param data
	 * @return
	 */
	public static ResultDto authError(@Nullable Object data) {
		return new ResultDto("99", "系统错误", data);
	}
	public static ResultDto fail(@Nullable Object data) {
		return new ResultDto("9", "系统错误", data);
	}
	public static ResultDto error(@Nullable Object data) {
		return new ResultDto("99", "系统错误", data);
	}
}
