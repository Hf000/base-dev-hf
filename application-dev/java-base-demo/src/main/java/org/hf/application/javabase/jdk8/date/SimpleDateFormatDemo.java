package org.hf.application.javabase.jdk8.date;

import org.hf.application.javabase.utils.ThreadPoolUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p> SimpleDateFormatDemo </p >
 * 问题: 如果将SimpleDateFormat对象用static修饰, 那么在多线程下会出现转换问题和取值差异问题
 * 原因: static修饰SimpleDateFormat对象,会出现多线程共享同一个变量
 * 解决: DateTimeFormatterDemo
 *
 * @author hufei
 * @date 2022-09-06
 **/
public class SimpleDateFormatDemo {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String format(Date date) {
        return SIMPLE_DATE_FORMAT.format(date);
    }

    public static Date parse(String date) throws ParseException {
        return SIMPLE_DATE_FORMAT.parse(date);
    }

    public static void main(String[] args) {
        ExecutorService executorService = ThreadPoolUtil.getJdkThreadPool();
        Date date = new Date();
        for (int i = 0; i < 30; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 10; j++) {
                    String format = format(date);
                    Date parse = null;
                    try {
                        parse = parse(format);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println(parse);
                }
            });
        }
        ThreadPoolUtil.closeJdkThreadPool(executorService);
    }
}
