package org.hf.application.custom.shop.dao;

import org.hf.application.custom.shop.domain.Item;

/**
 * <p> 商品数据接口 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:57
 */
public interface ItemDao {

    /**
     * 修改商品库存
     * @param count 数量
     * @param id 商品id
     */
    void modify(Integer count, String id);

    /**
     * 根据ID查找商品
     * @param id 商品id
     * @return 商品信息
     */
    Item findById(String id);
}
