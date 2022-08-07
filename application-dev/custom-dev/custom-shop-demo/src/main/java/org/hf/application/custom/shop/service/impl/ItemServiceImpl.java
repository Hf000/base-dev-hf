package org.hf.application.custom.shop.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hf.application.custom.shop.dao.ItemDao;
import org.hf.application.custom.shop.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 20:01
*/
@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDao itemDao;

    /***
     * 修改库存
     */
    @Override
    public int modify(Integer count, String id){
        int modifyCount = itemDao.modify(count,id);
        return modifyCount;
    }

}
