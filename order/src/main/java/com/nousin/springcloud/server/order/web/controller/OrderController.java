package com.nousin.springcloud.server.order.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单Controller
 *
 * @author Nousin
 * @since 2019/12/8
 */
@RestController
@RequestMapping("${nousin.base.requestUrl}")
//@RefreshScope // 配置文件中配置了 management.endpoints.web.exposure.include=refresh 时，
// 执行 curl -X POST http://localhost:8001/bus/refresh 请求时就会刷新 ${hello.word} 的值
public class OrderController {
    @Value("${nousin.hello.word:hello}")
    private String word;

    @GetMapping("/member")
    public Principal user(Principal member) {
        return member;
    }

    @GetMapping("hello")
    @PreAuthorize("hasAnyAuthority('hello')")
    public String hello(){
        return "hello";
    }

    /**
     * TODO
     *
     * @param param
     * @return
     */
    @RequestMapping(value="get", method = RequestMethod.GET)
    public Object get(@RequestParam String param){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code",1);
        resultMap.put("param",param);
        resultMap.put("word",word);
        return resultMap;
    }
}
