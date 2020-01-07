package com.nousin.springcloud.server.storage.web.controller;

import com.alibaba.fastjson.JSON;
import com.nousin.springcloud.common.dto.ResultDto;
import com.nousin.springcloud.common.util.ResultUtil;
import com.nousin.springcloud.server.storage.framework.common.util.I18nUtil;
import com.nousin.springcloud.server.storage.framework.common.util.SecurityContextUtil;
import com.nousin.springcloud.server.storage.framework.security.dao.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * TODO
 *
 * @author Nousin
 * @since 2019/12/25
 */
@RestController
@RequestMapping("${nousin.base.requestUrl}")
public class PermissionController {
    @Autowired
    PermissionMapper permissionMapper;

    @Autowired
    private MessageSource messageSource;
    /**
     * 测试国际化
     * @return
     */
    @RequestMapping(value="test", method = RequestMethod.GET)
    public Object test(HttpServletRequest request){
        String message = I18nUtil.getMessage("hello.world");
        return ResultUtil.success(message);
    }


    /**
     * TODO
     * 
     * @param param
     * @return
     */
    @PreAuthorize("hasAuthority('GET:ALL')")
    @RequestMapping(value="/get", method = RequestMethod.GET)
    public ResultDto get(@RequestParam(required = false) String param){
        return ResultUtil.success(permissionMapper.listPermissions());
    }


    @RequestMapping(value="/sys/dict", method = RequestMethod.GET)
    public ResultDto getPer(@RequestParam(required = false) String param){
        return ResultUtil.success(permissionMapper.listPermissionsById(SecurityContextUtil.getUser().getId()));
    }
}
