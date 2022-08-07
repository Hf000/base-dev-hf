package org.hf.application.javabase.apply.five.news;


public class ReportExcelClient {
    public static void main(String[] args) {
        ReportInterface reportInterface= new DataReadImpl();
        reportInterface.reportExcel();
    }
}
