package com.nousin.springcloud.server.storage.framework.exception;

import com.nousin.springcloud.common.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 服务器异常统一处理类
 *
 * @author Nousin
 * @since 2019/12/18
 */
@Slf4j
@RestControllerAdvice
@Order(2)
public class GlobalExceptionHandler {

    /**
     * 没有权限调用方法产生的异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public Object accessDeniedException(Exception e) {
        return ResultUtil.authAccessDenied();
    }

    /**
     * 通用异常拦截处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public Object exception(Exception e) {
        log.error("服务器发生异常，异常信息为：", e);
        return ResultUtil.error();
    }
}
