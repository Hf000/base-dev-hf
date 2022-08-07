package org.hf.application.javabase.apply.seven.news;


public class OrderService {

    private OrderDao orderDao;
    private LogDao logDao;

    public void add(){
        orderDao.add();
        logDao.recode();
    }
}
