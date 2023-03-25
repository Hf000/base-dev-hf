package org.hf.application.custom.shop.dao.impl;

import org.hf.application.custom.shop.dao.ItemDao;
import org.hf.application.custom.shop.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * <p> 商品数据接口实现 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:58
 */
@Repository
public class ItemDaoImpl implements ItemDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void modify(Integer count, String id) {
        jdbcTemplate.update("UPDATE item SET count=count-? WHERE id=?", count, id);
    }

    @Override
    public Item findById(String id) {
        return jdbcTemplate.queryForObject("SELECT * FROM item WHERE id=?", new BeanPropertyRowMapper<Item>(Item.class), id);
    }
}
