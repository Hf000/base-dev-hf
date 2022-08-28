package org.hf.application.javabase.jdk8.date;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DateTimeFormatterDemo {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String format(LocalDateTime date){
        return dateTimeFormatter.format(date);
    }

    public static LocalDateTime parse(String date) {
        return LocalDateTime.parse(date,dateTimeFormatter);
    }

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        for(int i=0;i<30;i++){

            executorService.execute(()->{

                for(int j=0;j<10;j++){

                    String format = format(LocalDateTime.now());
                    LocalDateTime parse = parse(format);
                    System.out.println(parse);
                }
            });
        }

        executorService.shutdown();
    }
}
