package org.hf.application.custom.shop.service;

/**
 * <p> 商品业务接口 </p>
 *
 * @author hufei
 * @date 2022/7/17 20:04
 */
public interface ItemService {

    /**
     * 修改库存
     * @param count 数量
     * @param id    商品id
     */
    void modify(Integer count, String id);
}
