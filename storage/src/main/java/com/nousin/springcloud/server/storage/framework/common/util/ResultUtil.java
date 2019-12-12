package com.nousin.springcloud.server.storage.framework.common.util;

import com.nousin.springcloud.server.storage.framework.common.dto.ResultDto;

/**
 * ResultDto 工具类
 *
 * @author tangwc
 * @since 2019/12/12
 */
public class ResultUtil {
    /**
     * 成功
     * @param data
     * @return
     */
    public static ResultDto success(Object data) {
        return new ResultDto("0", "操作成功", data);
    }
    /**
     * 系统错误
     * @param msg
     * @return
     */
    public static ResultDto error(String msg) {
        return new ResultDto("99", msg, "");
    }
}
