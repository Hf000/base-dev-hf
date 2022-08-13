package org.hf.application.mongodb.springdata.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p> 日期工具类 </p>
 * @author hufei
 * @date 2022/8/6 21:05
*/
@Slf4j
public class DateUtil {

    public static String getCurrentTime() {
        String time = "";
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            time = sdf.format(date);
        } catch (Exception e) {
            log.error("生成当前日期出错，格式yyyy-MM-dd hh:mm:ss", e);
        }
        return time;
    }

}
