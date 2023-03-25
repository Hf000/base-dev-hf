package org.hf.application.custom.shop.dao.impl;

import org.hf.application.custom.shop.dao.CouponsDao;
import org.hf.application.custom.shop.domain.Coupons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * <p> 优惠券数据接口实现 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:58
 */
@Repository
public class CouponsDaoImpl implements CouponsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Coupons findByIdAndUserName(String id, String username) {
        return jdbcTemplate.queryForObject("SELECT * FROM coupons WHERE username=? AND id=?  AND `status`=1", new BeanPropertyRowMapper<Coupons>(Coupons.class), username, id);
    }

    @Override
    public void modifyCouponsStatus(String id) {
        jdbcTemplate.update("UPDATE coupons SET `status`=2 , usetime=now() WHERE id=?", id);
    }
}
