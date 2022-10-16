package org.hf.application.custom.order.pojo.dto;

import org.hf.application.custom.order.pojo.entity.Orders;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class OrderDto implements Delayed {
    private int id;
    private long invalid;

    public OrderDto(Orders o){
        this.id = o.getId();
        this.invalid = o.getInvalid()*1000 + System.currentTimeMillis();
    }

    //倒计时，降到0时队列会吐出该任务
    @Override
    public long getDelay(TimeUnit unit) {
        return invalid - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        OrderDto o1 = (OrderDto) o;
        return this.invalid - o1.invalid <= 0 ? -1 : 1;
    }

    public int getId() {
        return id;
    }

    public long getInvalid() {
        return invalid;
    }
}
