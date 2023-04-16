package org.hf.boot.springboot.pojo.bo;

import lombok.Data;

/**
 * <p> redis锁业务实体 </p >
 * 任务队列保存参数
 *
 * @author HUFEI
 * @date 2023-03-27
 **/
@Data
public class RedisLockBO {

    /**
     * 业务唯一 key
     */
    private String businessKey;

    /**
     * 加锁时间 (秒 s)
     */
    private Long lockTime;

    /**
     * 上次更新时间（ms）
     */
    private Long lastModifyTime;

    /**
     * 保存当前线程
     */
    private Thread currentTread;

    /**
     * 总共尝试次数
     */
    private int tryCount;

    /**
     * 当前尝试次数
     */
    private int currentCount;

    /**
     * 更新的时间周期（毫秒）,公式 = (加锁时间（转成毫秒） / 3) * 2
     */
    private Long modifyPeriod;

    public RedisLockBO(String businessKey, Long lockTime, Long lastModifyTime, Thread currentTread, int tryCount) {
        this.businessKey = businessKey;
        this.lockTime = lockTime;
        this.lastModifyTime = lastModifyTime;
        this.currentTread = currentTread;
        this.tryCount = tryCount;
        this.modifyPeriod = (lockTime * 1000 / 3) * 2;
    }
}