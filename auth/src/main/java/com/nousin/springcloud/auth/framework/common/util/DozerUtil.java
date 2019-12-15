package com.nousin.springcloud.auth.framework.common.util;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.nousin.springcloud.auth.framework.config.SpringContextHolder;
import org.dozer.Mapper;

/**
 * @description: 对象转换工具类
 * @author: tangwc
 * @date: 2019/7/25
 */
public class DozerUtil {

    private static Mapper dozerMapper = SpringContextHolder.getBean(Mapper.class);

    /**
     *
     * @param obj 数据源
     * @param tClass 返回对象类型Class
     * @param <T> 返回对象类型
     * @return 新的T对象
     */
    public static <T> T map(Object obj, Class<T> tClass){
        return dozerMapper.map(obj,tClass);
    }

    /**
     * 基于Dozer转换Collection中对象的类型.
     * @param sourceList
     * @param destinationClass
     * @param <T>
     * @return List<T>
     */
    public static <T> List<T> mapList(Collection<?> sourceList, Class<T> destinationClass) {
        List<T> destinationList = Lists.newArrayList();
        for (Object sourceObject : sourceList) {
            T destinationObject = dozerMapper.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

}
