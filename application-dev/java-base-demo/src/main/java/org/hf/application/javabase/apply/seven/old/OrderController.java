package org.hf.application.javabase.apply.seven.old;


public class OrderController {

    private GoodsDao goodsDao;
    private OrderDao orderDao;
    private LogDao logDao;

    //添加订单
    public void add(){
        //①修改库存
        goodsDao.modify();
        //②增加订单
        orderDao.add();
        //③记录日志
        logDao.recode();
    }

}
