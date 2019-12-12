package com.nousin.springcloud.server.storage.framework.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 结果统一返回Dto
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultDto {
	private String code;
	private String msg;
	private Object data;
}
