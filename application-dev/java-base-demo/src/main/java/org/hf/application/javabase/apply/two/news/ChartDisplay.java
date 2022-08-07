package org.hf.application.javabase.apply.two.news;

import java.util.Scanner;


public class ChartDisplay {

    private static AbstractChart abstractChart;

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String className=sc.nextLine();

        //根据输入的类名获取对应实例
        setAbstractChart(className);

        //调用方法
        abstractChart.display();
    }

    /***
     * 根据需要创建实例
     */
    public static void setAbstractChart(String className) throws Exception {
        abstractChart = (AbstractChart) Class.forName(className).newInstance();
    }
}
