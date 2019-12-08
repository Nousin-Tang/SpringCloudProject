package com.nousin.springcloud.server.order.controller;

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
public class OrderController {
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
        return resultMap;
    }
}
