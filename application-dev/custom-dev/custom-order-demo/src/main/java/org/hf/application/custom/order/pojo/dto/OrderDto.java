package org.hf.application.custom.order.pojo.dto;

import org.hf.application.custom.order.pojo.entity.Orders;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * <p> 队列对象 </p>
 * 订单取消:借助延时队列来实现
 *
 * @author hufei
 * @date 2023/3/25 17:15
 */
public class OrderDto implements Delayed {

    private final int id;
    private final long invalid;

    public OrderDto(Orders o) {
        this.id = o.getId();
        this.invalid = o.getInvalid() * 1000 + System.currentTimeMillis();
    }

    /**
     * 倒计时，降到0时队列会吐出该任务
     * @param unit 时间单位枚举
     * @return 时间差 毫秒
     */
    @Override
    public long getDelay(@NotNull TimeUnit unit) {
        return invalid - System.currentTimeMillis();
    }

    /**
     * 比较哪个失效时间较短
     * @param delayed 比较对象
     * @return int
     */
    @Override
    public int compareTo(@NotNull Delayed delayed) {
        OrderDto dto = (OrderDto) delayed;
        return this.invalid - dto.invalid <= 0 ? -1 : 1;
    }

    public int getId() {
        return id;
    }

    public long getInvalid() {
        return invalid;
    }
}
