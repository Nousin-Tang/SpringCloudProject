package com.nousin.springcloud.auth.web.controller;

import com.nousin.springcloud.common.util.ResultUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 测试代码
 * 获取token：http://localhost:9002/oauth/token?grant_type=password&username=ad8min&password=123456
 * 代码中可以通过 SecurityContextHolder.getContext() 获取验证信息上下文
 * @author Nousin
 * @since 2019/12/12
 */
@RestController
@RequestMapping("${nousin.base.requestUrl}")
public class AuthController {
    /**
     * TODO
     *
     * @param param
     * @return
     */
    @RequestMapping(value="get", method = RequestMethod.GET)
    public Object get(@RequestParam String param, HttpServletRequest request){
        return ResultUtil.success(param);
    }
}
