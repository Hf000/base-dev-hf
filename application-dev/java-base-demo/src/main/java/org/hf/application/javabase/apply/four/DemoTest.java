package org.hf.application.javabase.apply.four;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class DemoTest {

    public static void main(String[] args) {
        ApplicationContext act = new ClassPathXmlApplicationContext("spring.xml");
        ChartDisplay chartDisplay = (ChartDisplay) act.getBean("chartDisplay");
        chartDisplay.showChart();
    }
}
