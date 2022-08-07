package org.hf.application.custom.shop.dao;

import org.hf.application.custom.shop.domain.Item;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 19:57
*/
public interface ItemDao {
    /***
     * 修改商品库存
     * @param count
     * @param id
     * @return
     */
    int modify(Integer count,String id);

    /***
     * 根据ID查找商品
     * @param id
     * @return
     */
    Item findById(String id);
}
