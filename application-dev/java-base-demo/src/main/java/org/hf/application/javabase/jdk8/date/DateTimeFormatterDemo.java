package org.hf.application.javabase.jdk8.date;

import org.hf.application.javabase.utils.ThreadPoolUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p> DateTimeFormatterDemo </p >
 * 解决SimpleDateFormat多线程情况下出现的问题
 * LocalDate、LocalTime、LocalDateTime都可以线程安全的操作日期和时间
 *
 * @author hufei
 * @date 2022-09-06
 **/
public class DateTimeFormatterDemo {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String format(LocalDateTime date) {
        return DATE_TIME_FORMATTER.format(date);
    }

    public static LocalDateTime parse(String date) {
        return LocalDateTime.parse(date, DATE_TIME_FORMATTER);
    }

    public static void main(String[] args) {

        ExecutorService executorService = ThreadPoolUtil.getJdkThreadPool();
        for (int i = 0; i < 30; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 10; j++) {
                    String format = format(LocalDateTime.now());
                    LocalDateTime parse = parse(format);
                    System.out.println(parse);
                }
            });
        }
        ThreadPoolUtil.closeJdkThreadPool(executorService);
    }
}
