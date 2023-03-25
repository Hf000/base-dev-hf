package org.hf.application.custom.shop.dao;

import org.hf.application.custom.shop.domain.Coupons;

/**
 * <p> 优惠券数据接口 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:59
 */
public interface CouponsDao {

    /**
     * 查询用户有效的优惠券
     * @param id  优惠券id
     * @param username 用户名称
     * @return 优惠券信息
     */
    Coupons findByIdAndUserName(String id, String username);

    /**
     * 修改指定优惠券状态
     * @param id 优惠券id
     */
    void modifyCouponsStatus(String id);
}
