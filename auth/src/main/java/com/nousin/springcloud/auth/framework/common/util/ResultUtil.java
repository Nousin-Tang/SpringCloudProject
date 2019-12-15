package com.nousin.springcloud.auth.framework.common.util;

import com.nousin.springcloud.auth.framework.common.dto.ResultDto;
import org.springframework.lang.Nullable;

/**
 * 结果返回工具类
 *
 * @author tangwc
 * @since 2019/12/13
 */
public class ResultUtil {
    /**
     * 操作成功
     *
     * @param data
     * @return
     */
    public static ResultDto success(Object data) {
        return new ResultDto("0", "操作成功", data);
    }

    public static ResultDto success(String message, Object data) {
        return new ResultDto("0", "操作成功", data);
    }

    /**
     * 系统错误
     *
     * @param data
     * @return
     */
    public static ResultDto error(@Nullable Object data) {
        return new ResultDto("99", "系统错误", data);
    }
}
