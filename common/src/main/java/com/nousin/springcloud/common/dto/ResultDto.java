package com.nousin.springcloud.common.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 结果统一返回Dto
 */
@Getter
@Setter
@AllArgsConstructor
public class ResultDto {
	private String resultCode;
	private String msg;
	private Object data;
}
