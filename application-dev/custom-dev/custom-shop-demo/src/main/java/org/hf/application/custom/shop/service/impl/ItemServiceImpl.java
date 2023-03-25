package org.hf.application.custom.shop.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hf.application.custom.shop.dao.ItemDao;
import org.hf.application.custom.shop.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p> 商品业务接口实现 </p>
 *
 * @author hufei
 * @date 2022/7/17 20:01
 */
@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDao itemDao;

    @Override
    public void modify(Integer count, String id) {
        itemDao.modify(count, id);
    }

}
