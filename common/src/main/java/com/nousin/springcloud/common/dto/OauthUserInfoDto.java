package com.nousin.springcloud.common.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Spring Security UserDetail实现
 *
 * @author Nousin
 * @since 2019/12/16
 */
@Getter
@Setter
public class OauthUserInfoDto {
    private String id; // 用户Id
    private String name; // 用户名称
    private String language; // 语言种类
    private String platform; // 平台
    private String partnerCode; // 合作伙伴编号
    private String tenantCode; // 商户编号
}
