package com.nousin.springcloud.common.util;

import com.nousin.springcloud.common.dto.ResultDto;

/**
 * 结果返回工具类
 *
 * @author tangwc
 * @since 2019/12/16
 */
public class ResultUtil {
	// ----------------------- 成功 -----------------------
	public static ResultDto success(Object data) {
		return new ResultDto("0", "操作成功", data);
	}

	public static ResultDto success(String message, Object data) {
		return new ResultDto("0", message, data);
	}

	// ----------------------- 失败 -----------------------

	// --------- 系统错误 ---------
	public static ResultDto error(Object data) {
		return new ResultDto("9999", "系统错误", data);
	}
	public static ResultDto fail(String code, String message) {
		return new ResultDto(code, message, null);
	}
	public static ResultDto fail(String message) {
		return new ResultDto("9", message, null);
	}

	// --------- 权限认证或校验错误 ---------
	public static ResultDto authError() {
		return new ResultDto("99", "认证系统繁忙", null);
	}
	public static ResultDto authFailed(String code, String message) {
		return new ResultDto(code, message, null);
	}


}
