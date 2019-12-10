package com.nousin.springcloud.server.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单Controller
 *
 * @author tangwc
 * @since 2019/12/8
 */
@RestController
@RequestMapping("/order")
//@RefreshScope // 配置文件中配置了 management.endpoints.web.exposure.include=refresh 时，
// 执行 curl -X POST http://localhost:8001/bus/refresh 请求时就会刷新 ${hello.word} 的值
public class OrderController {
    @Value("${hello.word:hello}")
    private String word;

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
