package org.hf.application.custom.order.component;

import org.hf.application.custom.order.mapper.FlashorderMapper;
import org.hf.application.custom.order.mapper.ProductMapper;
import org.hf.application.custom.order.pojo.entity.Flashorder;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p> RabbitMQ的消息队列监听实现 </p>
 *
 * @author hufei
 * @date 2023/3/25 17:34
 */
@Component
@RabbitListener(queues = "promotion.order")
public class PromotionMonitor {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private FlashorderMapper flashorderMapper;

    @RabbitHandler
    @Transactional(rollbackFor = Exception.class)
    public void receive(String msg) {
        if (msg != null && msg.length() != 0) {
            System.out.println("get it! " + msg);
            String[] ids = msg.split(",");
            Flashorder flashorder = new Flashorder();
            flashorder.setProductid(Integer.valueOf(ids[0]));
            flashorder.setUserid(Integer.valueOf(ids[1]));
            //入抢购订单表
            flashorderMapper.insert(flashorder);
            //db减库存
            productMapper.decr(Integer.parseInt(ids[0]));
        }
    }
}