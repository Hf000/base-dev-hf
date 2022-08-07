package org.hf.application.javabase.apply.seven.news;


public class GoodsService {
    private GoodsDao goodsDao;
    private OrderDao orderDao;

    public void modify(){
        goodsDao.modify();
        orderDao.add();
    }
}
