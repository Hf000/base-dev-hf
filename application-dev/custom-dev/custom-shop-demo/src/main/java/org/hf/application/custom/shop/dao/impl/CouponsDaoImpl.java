package org.hf.application.custom.shop.dao.impl;

import org.hf.application.custom.shop.dao.CouponsDao;
import org.hf.application.custom.shop.domain.Coupons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 19:58
*/
@Repository
public class CouponsDaoImpl implements CouponsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /***
     * 查询用户有效的优惠券
     * @param id
     * @param username
     * @return
     */
    @Override
    public Coupons findByIdAndUserName(String id, String username) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM coupons WHERE username=? AND id=?  AND `status`=1",new BeanPropertyRowMapper<Coupons>(Coupons.class),username,id);
        } catch (Exception e) {
        }
        return null;
    }

    /****
     * 修改指定优惠券状态
     * @param id
     */
    @Override
    public void modifyCouponsStatus(String id) {
        jdbcTemplate.update("UPDATE coupons SET `status`=2 , usetime=now() WHERE id=?",id);
    }
}
