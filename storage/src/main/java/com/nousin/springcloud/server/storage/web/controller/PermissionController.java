package com.nousin.springcloud.server.storage.web.controller;

import com.nousin.springcloud.common.dto.ResultDto;
import com.nousin.springcloud.common.util.ResultUtil;
import com.nousin.springcloud.server.storage.framework.security.dao.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author Nousin
 * @since 2019/12/25
 */
@RestController
@RequestMapping("${nousin.base.request-path}")
public class PermissionController {
    @Autowired
    PermissionMapper permissionMapper;
    /**
     * TODO
     * 
     * @param param
     * @return
     */
    @PreAuthorize("hasAuthority('GET:ALL')")
    @RequestMapping(value="/get", method = RequestMethod.GET)
    public ResultDto get(@RequestParam(required = false) String param){
        return ResultUtil.success(permissionMapper.findAllPermissions());
    }
    @RequestMapping(value="/sys/dict", method = RequestMethod.GET)
    public ResultDto getPer(@RequestParam(required = false) String param){
        return ResultUtil.success(permissionMapper.findAllPermissions());
    }
}
