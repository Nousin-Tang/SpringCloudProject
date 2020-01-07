package com.nousin.springcloud.common.util;

import com.nousin.springcloud.common.dto.OauthUserInfoDto;

import java.util.Locale;

/**
 * 用户上下文工具类 （注意：需要主动释放资源）
 *
 * @author Nousin
 * @since 2019/12/16
 */
public class UserContextUtil {

    // 当前用户信息
    private static final ThreadLocal<OauthUserInfoDto> userThreadLocal = new ThreadLocal<>();
    // 当前用户附加信息
    private static final ThreadLocal<Locale> localeThreadLocal = new ThreadLocal<>();

    /**
     * 构造函数私有化
     */
    private UserContextUtil() {
    }

    /**
     * 用户上下文中放入信息
     *
     * @param dto
     */
    public static void setUserContext(OauthUserInfoDto dto) {
        userThreadLocal.set(dto);
    }

    /**
     * 获取上下文中的信息
     *
     * @return 上下文信息
     */
    public static OauthUserInfoDto getUserContext() {
        OauthUserInfoDto oauthUserInfoDto = userThreadLocal.get();
        if (null == oauthUserInfoDto) {
            OauthUserInfoDto defaultUserInfo = new OauthUserInfoDto();
            defaultUserInfo.setId("1");
            defaultUserInfo.setPartnerCode("KD");
            defaultUserInfo.setPlatform("partner");
            defaultUserInfo.setLanguage("en_US");
            return defaultUserInfo;
        }
        return oauthUserInfoDto;
    }

    /**
     * 获取上下文中的用户ID
     *
     * @return 用户ID
     */
    public static String getUserId() {
        return getUserContext().getId();
    }


    /**
     * 用户上下文中放入附加信息
     *
     * @param dto 参数
     */
    public static void setLocale(Locale dto) {
        localeThreadLocal.set(dto);
    }


    /**
     * 用户上下文中获取附加信息
     */
    public static Locale getLocale() {
        return localeThreadLocal.get();
    }


    /**
     * 清空上下文
     */
    public static void clear() {
        userThreadLocal.remove();
        localeThreadLocal.remove();
    }

}
