package org.hf.application.custom.order.component;

import org.hf.application.custom.order.mapper.OrdersMapper;
import org.hf.application.custom.order.pojo.dto.OrderDto;
import org.hf.application.custom.order.pojo.entity.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class OrderMonitor {
    @Autowired
    OrdersMapper mapper ;
    //延时队列
    final DelayQueue<OrderDto> queue = new DelayQueue<OrderDto>();
    //任务池
    ExecutorService service = Executors.newFixedThreadPool(3);

    //投放延迟订单
    public void put(OrderDto dto){
        this.queue.put(dto);
        System.out.println("put task:"+dto.getId());
    }

    //在构造函数中启动守护线程
    public OrderMonitor(){
        this.execute();
        System.out.println("order monitor started");
    }

    //守护线程
    public void execute(){
        new Thread(()->{
            while (true){
                try {
                    OrderDto dto = queue.take();
                    System.out.println("take task:"+dto.getId());
                    service.execute(new Task(dto));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //任务类
    class Task implements Runnable{
        OrderDto dto;
        Task(OrderDto dto){
            this.dto = dto;
        }
        @Override
        public void run() {
            Orders orders = new Orders();
            orders.setId(dto.getId());
            orders.setUpdatetime(new Date());
            orders.setStatus(-1);
            System.out.println("cancel order:"+orders.getId());
            mapper.updateByPrimaryKeySelective(orders);
        }
    }


}
