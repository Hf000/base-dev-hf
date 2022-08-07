package org.hf.application.custom.shop.dao;

import org.hf.application.custom.shop.domain.Coupons;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 19:59
*/
public interface CouponsDao {
    /***
     * 查询用户有效的优惠券
     * @param id
     * @param username
     * @return
     */
    Coupons findByIdAndUserName(String id, String username);

    /***
     * 修改指定优惠券状态
     * @param id
     */
    void modifyCouponsStatus(String id);
}
