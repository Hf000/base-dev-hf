package org.hf.boot.springboot.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * <p> 基于redis实现的分布式锁,支持单节点，主从，哨兵模式，不支持集群模式 哨兵模式在主从切换瞬间可能导致同一资源产生两把锁 </p >
 *
 * @author hufei
 * @date 2023-04-11
 **/
@Component
public class RedisDistributedLock {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 加锁的key前缀
     */
    private final static String PREFIX = "lock_";
    private static final Long RELEASE_SUCCESS = 1L;
    /**
     * 释放锁的脚本
     */
    private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    @Autowired(required = false)
    private StringRedisTemplate template;

    /**
     * 最大努力获取业务锁, 直到超时仍然取不到锁才才返回失败，适用于一定要等待到锁的业务场景
     *
     * @param lockName                  锁名称，同时也是缓存key,全局唯一，勿用过于简短的名字以防重复,建议至少分两段(projectName_businessName)
     * @param requestId                 请求ID，解锁时需用同一请求ID，建议用UUID生成
     * @param expiredInMilliSeconds     锁的有效毫秒数.设置比实际业务需要更长的时间，并在业务结束时调用releaseLock方法释放锁.以避免业务未完成，锁已超时失效；或业务已结束，仍然持有锁
     * @param maxWaitTimeInMilliSeconds 尝试获取锁的最长等待时间(毫秒)，超时返回获取锁失败
     * @return true - 获取锁成功
     */
    public boolean tryLock(String lockName, String requestId, long expiredInMilliSeconds, long maxWaitTimeInMilliSeconds) {
        long time = System.currentTimeMillis();
        while (!tryLock(lockName, requestId, expiredInMilliSeconds)) {
            if (System.currentTimeMillis() - time > maxWaitTimeInMilliSeconds) {// 超时返回false
                return false;
            }
            try {
                // 如果没有超过最大等待时间, 则暂停一下线程
                Thread.sleep(5L);
            } catch (InterruptedException e) {
                logger.warn("", e);
            }
        }
        return true;
    }

    /**
     * 获取业务锁, 如锁已被占用则快速返回失败
     *
     * @param lockName              锁名称，同时也是缓存key,全局唯一，勿用过于简短的名字以防重复,建议至少分两段(projectName.businessName)
     * @param requestId             请求ID，解锁时需用同一请求ID，建议用UUID生成, 全局唯一，勿用过于简短的名字以防重复,建议至少分两段(projectName.businessName)
     * @param expiredInMilliSeconds 锁的有效毫秒数.设置比实际业务需要更长的时间(或者在业务未完成但锁到期前调用renewal方法手动续期)，并在业务结束时调用releaseLock方法释放锁.以避免业务未完成，锁已超时失效；或业务已结束，仍然持有锁
     * @return boolean 取得锁后返回成功true
     */
    public boolean tryLock(final String lockName, final String requestId, final long expiredInMilliSeconds) {
        if (!StringUtils.hasLength(requestId) || !StringUtils.hasLength(lockName)) {
            throw new IllegalArgumentException("lockName and requestId can't be null");
        }
        try {
            Boolean lockFlag = template.opsForValue().setIfAbsent(PREFIX + lockName, requestId, expiredInMilliSeconds, TimeUnit.MILLISECONDS);
            return Boolean.TRUE.equals(lockFlag);
        } catch (Exception e) {
            logger.error("get lock {} fail", lockName, e);
            return false;
        }
    }

    /**
     * 锁续期，必须在锁的有效期内才能续期成功
     *
     * @param lockName              锁名称
     * @param requestId             请求ID，对应获取锁时设置的requestId
     * @param expiredInMilliSeconds 锁的新的有效毫秒数,从续约成功一刻算起
     * @return boolean 续期后返回成功true
     */
    public boolean renewal(final String lockName, final String requestId, final long expiredInMilliSeconds) {
        if (!StringUtils.hasLength(requestId) || !StringUtils.hasLength(lockName)) {
            throw new IllegalArgumentException("lockName and requestId can't be null");
        }
        try {
            String key = PREFIX + lockName;
            if (StringUtils.hasLength(requestId) && requestId.equals(template.opsForValue().get(key))) {
                template.expire(key, expiredInMilliSeconds, TimeUnit.MILLISECONDS);
                return true;
            } else {
                logger.error("renewal  fail for lock {} not exist", lockName);
            }
        } catch (Exception e) {
            logger.error("renewal for lock {} fail", lockName, e);
        }
        return false;
    }

    /**
     * 立即释放锁
     *
     * @param lockName  锁名称
     * @param requestId 请求ID
     */
    public boolean unLock(final String lockName, final String requestId) {
        if (!StringUtils.hasLength(requestId) || !StringUtils.hasLength(lockName)) {
            throw new IllegalArgumentException("lockName and requestId can't be null");
        }
        RedisScript<Long> releaseScript = new DefaultRedisScript<>(RELEASE_LOCK_SCRIPT, Long.class);
        Long result = template.execute(releaseScript, Collections.singletonList(PREFIX + lockName), requestId);
        logger.debug("release lock result: {}", result);
        if (RELEASE_SUCCESS.equals(result)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}