package com.nousin.springcloud.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * TODO
 *
 * @author Nousin
 * @since 2019/12/27
 */
public class DateUtil {

    public static final ZoneId zoneId = ZoneId.systemDefault();

    /**
     * 获取当前时间
     * @return
     */
    public static LocalDateTime now(){
        return LocalDateTime.now();
    }

    /**
     * 转 java.util.Date
     * @param localDateTime
     * @return
     */
    public static Date toDate(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    /**
     * 转 java.time.LocalDateTime
     * @param date
     * @return
     */
    public static LocalDateTime toLocalDateTime(Date date){
        return LocalDateTime.ofInstant(date.toInstant(),zoneId);
    }

}
