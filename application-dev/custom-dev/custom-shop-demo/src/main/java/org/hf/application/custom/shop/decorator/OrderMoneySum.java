package org.hf.application.custom.shop.decorator;

import org.hf.application.custom.shop.dao.ItemDao;
import org.hf.application.custom.shop.domain.Item;
import org.hf.application.custom.shop.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p> 价格计算 -- 原价格计算 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:59
 */
@Component(value = "orderMoneySum")
public class OrderMoneySum implements MoneySum {

    @Autowired
    private ItemDao itemDao;

    @Override
    public void money(Order order) {
        //查询商品
        Item item = itemDao.findById(order.getItemId());
        //商品价格*购买数量   订单价格
        order.setMoney(item.getPrice() * order.getNum());
        //结算价格
        order.setPayMoney(item.getPrice() * order.getNum());
    }
}
