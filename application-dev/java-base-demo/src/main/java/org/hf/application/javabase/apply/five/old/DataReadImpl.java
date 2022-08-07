package org.hf.application.javabase.apply.five.old;


public class DataReadImpl implements DataRead {
    @Override
    public void query() {
        System.out.println("查询");
    }

    @Override
    public void insert() {
        System.out.println("增加");
    }

    @Override
    public void delete() {
        System.out.println("删除");
    }

    @Override
    public void update() {
        System.out.println("修改");
    }

    @Override
    public void queryTask() {
        System.out.println("查询定时任务");
    }

    @Override
    public void createTask() {
        System.out.println("创建定时任务");
    }

    @Override
    public void reportExcel() {
        System.out.println("导出Excel报表");
    }
}
