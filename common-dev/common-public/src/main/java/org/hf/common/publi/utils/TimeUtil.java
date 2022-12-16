package org.hf.common.publi.utils;

import cn.hutool.core.date.DateUtil;
import org.hf.common.publi.constants.DateFormatConstant;
import org.hf.common.publi.constants.NumberConstant;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static org.hf.common.publi.constants.DateFormatConstant.TIME_UNIT_MS;
import static org.hf.common.publi.constants.DateFormatConstant.TIME_UNIT_S;
import static org.hf.common.publi.constants.DateFormatConstant.YYYY_MM_DD_HH_MM_SS;
import static org.hf.common.publi.constants.NumberConstant.INT_1000;

/**
 * <p> 计算时间工具类 </p >
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/9 16:40
 */
public class TimeUtil {

    //============================获取开始结束时间========================

    private long time;

    public TimeUtil() {
        // 初始化一个时间
        time = DateUtil.current();
    }

    public long getStartTime() {
        return time;
    }

    /**
     * 获取增量时间
     *
     * @return long
     */
    public long getDeltaTime() {
        long delta = DateUtil.current() - time;
        time = DateUtil.current();
        return delta;
    }

    public String getDeltaTimeText() {
        return getDeltaTime() + TIME_UNIT_MS;
    }

    public String getDeltaSecondText() {
        return getDeltaTime() / (double) INT_1000 + TIME_UNIT_S;
    }

//============================借助Calendar类获取特定时间（返回类型为date类型）========================

    /**
     * 获取当天开始时间
     * @return Date
     */
    public static Date getDayBegin() {
        return getDayBegin(null);
    }

    /**
     * 获取指定日期的开始时间
     * @param date 入参
     * @return Date
     */
    public static Date getDayBegin(Date date) {
        // Calendar.getInstance() 默认创建的是一个 new GregorianCalendar()对象
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        //0点
        cal.set(Calendar.HOUR_OF_DAY, 0);
        //0分
        cal.set(Calendar.MINUTE, 0);
        //0秒
        cal.set(Calendar.SECOND, 0);
        //0毫秒
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取当天结束时间
     * @return Date
     */
    public static Date getDayEnd() {
        return getDayEnd(null);
    }

    /**
     * 获取指定日期的结束时间
     * @param date 入参
     * @return Date
     */
    public static Date getDayEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        //23点
        cal.set(Calendar.HOUR_OF_DAY, 23);
        //59分
        cal.set(Calendar.MINUTE, 59);
        //59秒
        cal.set(Calendar.SECOND, 59);
        //0毫秒
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取指定的整点时间
     * @param times 指定的时间点, 小于0获取的前一天的时间点, 大于23获取的是明天的时间点
     * @return Date
     */
    public static Date getTimes(int times) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, times);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当前日期的前或者后多少天的日期时间
     * @param days 指定天数, 负数为前多少天, 正数为后多少天
     * @return Date
     */
    public static Date currAddDays(int days) {
        return addDateDays(null, days);
    }

    /**
     * 获取指定日期的前或者后多少天的日期时间
     * @param date 指定日期时间
     * @param days 指定天数, 负数为前多少天, 正数为后多少天
     * @return Date
     */
    public static Date addDateDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        // 设置时间
        if (date != null) {
            cal.setTime(date);
        }
        // 当天月份天数加传入天数
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    /**
     * 获取指定日期增加指定小时后的日期时间
     * @param date  指定日期
     * @param hours 指定增加的小时数
     * @return Date
     */
    public static Date addDateHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 24小时制
        calendar.add(Calendar.HOUR, hours);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取指定日期增加指定分钟后的日期时间
     * @param date  指定日期
     * @param minutes 指定增加的分钟数
     * @return Date
     */
    public static Date addDateMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 24小时制
        calendar.add(Calendar.MINUTE, minutes);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取今年是哪一年
     * @return Integer
     */
    public static Integer getNowYear() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取本月是哪一月
     * @return int
     */
    public static int getNowMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // 由于月份是从0开始, 所以这里需要加1
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取指定日期时间是第几季度
     * @param date 指定日期
     * @return int
     */
    public static int getDateSeason(Date date) {
        final int[] season = {1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return season[cal.get(Calendar.MONTH)];
    }

    /**
     * 获取指定年月指定间隔天数的日期时间列表
     * @param beginYear     年
     * @param beginMonth    月 1-12月
     * @param intervalDay   间隔天数
     * @return List<Date>
     */
    public static List<Date> getDateList(int beginYear, int beginMonth, int intervalDay) {
        if (intervalDay <= 0) {
            intervalDay = 1;
        }
        if (beginMonth < 1 || beginMonth > 12) {
            return new ArrayList<>();
        } else {
            beginMonth -= 1;
        }
        List<Date> list = new ArrayList<>();
        Calendar beginCal = new GregorianCalendar(beginYear, beginMonth, 1);
        // 获取月的天数
        int max = beginCal.getActualMaximum(Calendar.DATE);
        for (int i = 1; i <= max; i = i + intervalDay) {
            list.add(beginCal.getTime());
            beginCal.add(Calendar.DATE, intervalDay);
        }
        return list;
    }

    /**
     * 获取指定开始年月到结束年月的指定间隔天数的日期时间列表
     * @param beginYear  开始年
     * @param beginMonth 开始月
     * @param endYear    结束年
     * @param endMonth   结束月
     * @param intervalDay 间隔天数
     * @return List<List<Date>>
     */
    public static List<List<Date>> getDateList(int beginYear, int beginMonth, int endYear, int endMonth, int intervalDay) {
        List<List<Date>> list = new ArrayList<>();
        if (beginYear == endYear) {
            for (int j = beginMonth; j <= endMonth; j++) {
                list.add(getDateList(beginYear, j, intervalDay));
            }
        } else {
            for (int j = beginMonth; j <= 12; j++) {
                list.add(getDateList(beginYear, j, intervalDay));
            }
            for (int i = beginYear + 1; i < endYear; i++) {
                for (int j = 1; j <= 12; j++) {
                    list.add(getDateList(i, j, intervalDay));
                }
            }
            for (int j = 1; j <= endMonth; j++) {
                list.add(getDateList(endYear, j, intervalDay));
            }
        }
        return list;
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return List<Date>
     */
    public static List<Date> getDateList(Date beginDate, Date endDate) {
        List<Date> dateList = new ArrayList<>();
        // 把开始时间加入集合
        dateList.add(beginDate);
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        // 判断结束日期是否在指定日期之后
        while (endDate.after(cal.getTime())) {
            dateList.add(cal.getTime());
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        // 把结束时间加入集合
        dateList.add(endDate);
        return dateList;
    }

//============================时间计算========================

    /**
     * 两个日期相减得到的天数
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     */
    public static int getDiffDays(Date beginDate, Date endDate) {
        if (beginDate == null || endDate == null) {
            throw new IllegalArgumentException("getDiffDays param is null!");
        }
        long diff = (endDate.getTime() - beginDate.getTime()) / (1000 * 60 * 60 * 24);
        return new Long(diff).intValue();
    }


    /**
     * 两个日期相减得到的毫秒数
     */
    public static long dateDiff(Date beginDate, Date endDate) {
        long date1ms = beginDate.getTime();
        long date2ms = endDate.getTime();
        return date2ms - date1ms;
    }


    /**
     * 获取两个日期中的最大日起
     */
    public static Date max(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        //beginDate日期大于endDate
        if (beginDate.after(endDate)) {
            return beginDate;
        }
        return endDate;
    }

    /**
     * 获取两个日期中的最小日期
     */
    public static Date min(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return endDate;
        }
        return beginDate;
    }

    /**
     * 判断是否闰年
     * 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
     */
    public static boolean isLeapYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        if ((year % 400) == 0) {
            // 世纪闰年
            return true;
        } else if ((year % 4) == 0) {
            // 普通闰年
            return (year % 100) != 0;
        } else {
            return false;
        }
    }

    //=================================时间格式转换==========================

    /**
     * 将时间转换成yyyy-MM-dd HH:mm:ss格式字符串
     * @param date 日期
     * @return String
     */
    public static String dateFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        return formatter.format(date);
    }

    /**
     * 将CST时间类型字符串格式化成yyyy-MM-dd HH:mm:ss
     * @param dataStr 时间字符串
     * @return String
     * @throws ParseException 异常
     */
    public static String cstFormat(String dataStr) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        return dateFormat(formatter.parse(dataStr));
    }

    /**
     * 将long类型转化为Date
     * @param time 时间戳
     * @return Date
     */
    public static Date longToDate(long time) {
        if (String.valueOf(time).length() == 10) {
            time = time * 1000;
        }
        return new Date(time);
    }
}