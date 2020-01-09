package com.nousin.springcloud.server.storage.framework.common.util;

import com.nousin.springcloud.common.util.UserContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

/**
 * 国际化工具类
 *
 * @author Nousin
 * @since 2020/1/7
 */
@Slf4j
public class I18nUtil {

    // message资源
    private static MessageSource messageSource = SpringContextUtil.getBean(MessageSource.class);
    // 默认解析参数
    private static final Object[] objs = null;

    /**
     * 获取国际化信息
     * @param key 对应键
     * @return 国际化信息
     */
    public static String getMessage(String key){
        return getMessage(key, UserContextUtil.getLocale(), objs);
    }

    /**
     * 获取国际化信息
     * @param key 对应键
     * @param objects 参数
     * @return 国际化信息
     */
    public static String getMessage(String key, Object...objects){
        return getMessage(key, UserContextUtil.getLocale(), objects);
    }

    /**
     * 获取国际化信息
     * @param key 对应键
     * @param locale 语言
     * @return 国际化信息
     */
    public static String getMessage(String key, Locale locale){
        return getMessage(key, locale, objs);
    }

    /**
     * 获取国际化信息
     * @param key 对应键
     * @param locale 语言
     * @param objects 参数
     * @return 国际化信息
     */
    public static String getMessage(String key, Locale locale, Object... objects){
        if (StringUtils.isEmpty(key)) {
            return StringUtils.EMPTY;
        }
        if (null == locale) {
            locale = Locale.getDefault();
        }
        try{
            return messageSource.getMessage(key, objects, locale);
        }catch (NoSuchMessageException e){
            try{
                return messageSource.getMessage(key, objects, Locale.getDefault());
            }catch (NoSuchMessageException ex){
                return "";
            }
        }
    }
}
