package org.hf.application.javabase.algorithm;

/**
 * 令牌桶限流算法  伪代码
 * 这里就没有预热,增长的速率往桶中放入令牌
 * 需要考虑线程安全问题
 */
public class TokenBucket {
    /**
     * 计算的获取令牌的开始时间
     */
    public long timeStamp = System.currentTimeMillis();
    /**
     * 令牌桶的容量
     */
    public long capacity;
    /**
     * 令牌放入速度
     */
    public long rate;
    /**
     * 当前令牌数量
     */
    public long tokens;

    public TokenBucket(long capacity, long rate) {
        this.capacity = capacity;
        this.rate = rate;
    }

    public boolean grant() {
        // 当前时间
        long now = System.currentTimeMillis();
        /*
         * 当前令牌数量 = 原先剩余的令牌 + 计算时间区间内向桶中放入的令牌数量,
         * 这里直接模拟了一定时间内向桶中放令牌, 实际场景应该是根据设定的频率定时向桶中放令牌,然后直接判断桶中是否有令牌即可
         */
        tokens = Math.min(capacity, tokens + (now - timeStamp) * rate);
        // 更新timeStamp为当前计算截止时间
        timeStamp = now;
        if (tokens < 1) {
            // 若不到1个令牌,则拒绝
            return false;
        } else {
            // 还有令牌，领取令牌
            tokens--;
            return true;
        }
    }
}