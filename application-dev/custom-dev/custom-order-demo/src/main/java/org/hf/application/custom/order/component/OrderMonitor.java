package org.hf.application.custom.order.component;

import org.hf.application.custom.order.mapper.OrdersMapper;
import org.hf.application.custom.order.pojo.dto.OrderDto;
import org.hf.application.custom.order.pojo.entity.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p> 定义订单监控类, 启动守护线程,如果有超时任务,提交线程池处理任务 </p>
 *
 * @author hufei
 * @date 2023/3/25 17:23
 */
@Component
public class OrderMonitor {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private ThreadPoolTaskExecutor customTaskExecutor;

    /**
     * 定义订单延时队列
     */
    final DelayQueue<OrderDto> queue = new DelayQueue<>();

    /**
     * bean加载后,立即启动守护线程
     */
    @PostConstruct
    public void initOrderMonitor() {
        this.execute();
        System.out.println("order monitor started");
    }

    /**
     * 守护线程
     */
    public void execute() {
        CompletableFuture.runAsync(() -> {
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    OrderDto dto = queue.take();
                    System.out.println("take task:" + dto.getId());
                    customTaskExecutor.execute(new Task(dto));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, customTaskExecutor);
    }

    /**
     * 处理任务类
     */
    class Task implements Runnable {
        OrderDto dto;

        Task(OrderDto dto) {
            this.dto = dto;
        }

        @Override
        public void run() {
            Orders orders = new Orders();
            orders.setId(dto.getId());
            orders.setUpdatetime(new Date());
            orders.setStatus(-1);
            System.out.println("cancel order:" + orders.getId());
            ordersMapper.updateByPrimaryKeySelective(orders);
        }
    }

    /**
     * 投放延迟订单
     * @param dto 订单信息
     */
    public void put(OrderDto dto) {
        this.queue.put(dto);
        System.out.println("put task:" + dto.getId());
    }
}
