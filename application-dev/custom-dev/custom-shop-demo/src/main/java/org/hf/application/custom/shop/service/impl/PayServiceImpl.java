package org.hf.application.custom.shop.service.impl;

import org.hf.application.custom.shop.dao.OrderDao;
import org.hf.application.custom.shop.domain.Order;
import org.hf.application.custom.shop.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 20:02
*/
@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private OrderDao orderDao;

    /***
     * 支付
     * @param type
     * @param id
     */
    @Override
    public void pay(String type, String id) {
        //查询订单
        Order order = orderDao.findById(id);

        //支付-》

        //修改订单状态 0未支付，1已支付
        orderDao.modifyStatus(id,1);

        //改变订单状态，通知发货
    }
}
