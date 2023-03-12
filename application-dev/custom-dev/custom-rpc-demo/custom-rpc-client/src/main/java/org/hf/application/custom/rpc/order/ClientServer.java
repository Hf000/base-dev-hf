package org.hf.application.custom.rpc.order;

import org.hf.application.custom.rpc.core.client.BeanFactory;
import org.hf.application.custom.rpc.core.client.NettyClient;
import org.hf.application.custom.rpc.order.pojo.Item;
import org.hf.application.custom.rpc.order.pojo.Order;
import org.hf.application.custom.rpc.order.service.OrderService;

import java.util.ArrayList;
import java.util.List;

public class ClientServer {

    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();

        nettyClient.start("127.0.0.1", 5566);

        BeanFactory beanFactory = new BeanFactory(nettyClient);

        OrderService orderService = beanFactory.getBean(OrderService.class);

        List<Item> itemList = new ArrayList<>();
        Item item = new Item();
        item.setItemId(2001L);
        item.setPrice(100L);
        item.setTitle("铅笔");
        itemList.add(item);

        item = new Item();
        item.setItemId(2002L);
        item.setPrice(50L);
        item.setTitle("橡皮");
        itemList.add(item);

        for (int i = 0; i < 10; i++) {
            Order order = orderService.submitOrder(1001L, itemList);
            System.out.println("返回数据：" + order);
        }

        nettyClient.close();

    }

}