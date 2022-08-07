package org.hf.application.custom.shop.dao;

import org.hf.application.custom.shop.domain.Order;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 19:58
*/
public interface OrderDao {
    int add(Order order);

    /***
     * 修改订单状态
     * @param id
     */
    void modifyStatus(String id,int status);

    /***
     * 查找订单
     * @param id
     * @return
     */
    Order findById(String id);
}
