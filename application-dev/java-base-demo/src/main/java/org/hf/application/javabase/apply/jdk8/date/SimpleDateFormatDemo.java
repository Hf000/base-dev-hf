package org.hf.application.javabase.apply.jdk8.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleDateFormatDemo {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String format(Date date){
        return simpleDateFormat.format(date);
    }

    public static Date parse(String date) throws ParseException {

        return simpleDateFormat.parse(date);

    }

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        Date date = new Date();

        for(int i=0;i<30;i++){

            executorService.execute(()->{
                for(int j=0;j<10;j++){
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

        executorService.shutdown();
    }
}
