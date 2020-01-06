package com.nousin.springcloud.common.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 基础实体类
 *
 * @author Nousin
 * @since 2020/1/6
 */
@Getter
@Setter
public class BaseDo {
    private String id; // ID
    private Integer version; // 版本号
    private String delFlag; // 删除标记
    private Date createDate; // 创建时间
    private String createBy; // 创建人
    private Date updateDate; // 更新时间
    private String updateBy; // 更新人
}
