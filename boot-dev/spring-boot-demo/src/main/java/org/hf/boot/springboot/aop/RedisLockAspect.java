package org.hf.boot.springboot.aop;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hf.boot.springboot.annotations.CustomRedisLock;
import org.hf.boot.springboot.constants.RedisLockTypeEnum;
import org.hf.boot.springboot.pojo.bo.RedisLockBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p> Redis分布式锁切面处理 </p >
 *  2.自定义redis锁注解切面
 *  自定义redis锁CustomRedisLock实现 - 2
 * @author HUFEI
 * @date 2023-03-27
 **/
@Slf4j
@Component
@Aspect
public class RedisLockAspect {

    /**
     * 执行周期性任务线程池,队列采用延迟队列,创建线程时设置为守护线程，维护keyAliveTime
     */
    private static final ScheduledExecutorService SCHEDULER = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("redisLock-schedule-pool").daemon(true).build());

    /**
     * 存储目前有效的key定义
     */
    private static final ConcurrentLinkedQueue<RedisLockBO> HOLDER_LIST = new ConcurrentLinkedQueue<>();

    @Autowired
    private StringRedisTemplate redisTemplate;

    {
        // 两秒执行一次「续时」操作, Runnable:执行线程, initialDelay:初始化延时入参, period:执行时间间隔, unit:时间单位枚举
        SCHEDULER.scheduleAtFixedRate(() -> {
            try {
                // 这里记得加 try-catch，否者报错后定时任务将不会再执行=-=
                Iterator<RedisLockBO> iterator = HOLDER_LIST.iterator();
                while (iterator.hasNext()) {
                    RedisLockBO holder = iterator.next();
                    // 判空 或者 判断key是否还有效,无效的话进行移除
                    if (holder == null || redisTemplate.opsForValue().get(holder.getBusinessKey()) == null) {
                        iterator.remove();
                        continue;
                    }
                    // 超时重试次数，当前重试次数超过设定的重试次数时把线程中断
                    if (holder.getCurrentCount() > holder.getTryCount()) {
                        holder.getCurrentTread().interrupt();
                        iterator.remove();
                        continue;
                    }
                    // 判断是否进入最后三分之一时间  【最近更新时间】 +【失效时间的三分之二】 <= 【当前时间】 就需要续期
                    long curTime = System.currentTimeMillis();
                    boolean shouldExtend = (holder.getLastModifyTime() + holder.getModifyPeriod()) <= curTime;
                    if (shouldExtend) {
                        // 续期
                        holder.setLastModifyTime(curTime);
                        redisTemplate.expire(holder.getBusinessKey(), holder.getLockTime(), TimeUnit.SECONDS);
                        log.info("businessKey : [" + holder.getBusinessKey() + "], try count : " + holder.getCurrentCount());
                        holder.setCurrentCount(holder.getCurrentCount() + 1);
                    }
                }
            } catch (Exception exception) {
                log.error("锁的定时任务执行失败");
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    /**
     * //@annotation 中的路径表示拦截特定注解
     */
    @Pointcut("@annotation(org.hf.boot.springboot.annotations.CustomRedisLock)")
    public void redisLockAop() {
    }

    /**
     * 加锁
     * @param pjp 切点
     * @return 方法调用返回结果
     */
    @SneakyThrows
    @Around(value = "redisLockAop()")
    public Object around(ProceedingJoinPoint pjp) {
        String businessKey = null;
        Object result = null;
        try {
            // 解析参数
            Method method = resolveMethod(pjp);
            CustomRedisLock annotation = method.getAnnotation(CustomRedisLock.class);
            RedisLockTypeEnum typeEnum = annotation.typeEnum();
            Object[] params = pjp.getArgs();
            // 取默认参数作为key的组成部分
            String ukString = params[annotation.lockFiled()].toString();
            // 省略很多参数校验和判空
            businessKey = typeEnum.getUniqueKey(ukString);
            String uniqueValue = UUID.randomUUID().toString();
            // 判断是否加锁   如果key不存在(返回true)则新增，存在(返回false)则不改变已经有的值，同时设置过期时间
            Boolean isSuccess = redisTemplate.opsForValue().setIfAbsent(businessKey, uniqueValue, Duration.ofMinutes(1L));
            if (isSuccess == null || !isSuccess) {
                throw new RuntimeException("You can't do it，because another has get the lock =-= 请求频繁");
            }
            Thread currentThread = Thread.currentThread();
            HOLDER_LIST.add(new RedisLockBO(businessKey, annotation.lockTime(), System.currentTimeMillis(), currentThread, annotation.tryCount()));
            redisTemplate.expire(businessKey, annotation.lockTime(), TimeUnit.SECONDS);
            result = pjp.proceed();
            // 线程被中断，抛出异常，中断此次请求
            if (currentThread.isInterrupted()) {
                throw new InterruptedException("You had been interrupted =-=");
            }
        } catch (InterruptedException e) {
            log.error("Interrupt exception, rollback transaction", e);
            throw new Exception("Interrupt exception, please send request again");
        } catch (Exception e) {
            log.error("has some error, please check again", e);
        } finally {
            // 请求结束后，强制删掉 key，释放锁
            releaseValidKey(businessKey, Thread.currentThread());
        }
        return result;
    }

    /**
     * 释放锁
     * @param businessKey   unique key
     * @param currentThread thread info
     */
    private void releaseValidKey(String businessKey, Thread currentThread) {
        try {
            RedisLockBO redisLockBO = HOLDER_LIST.stream().filter(h -> h.getBusinessKey().equals(businessKey)).findFirst().orElse(null);
            if (redisLockBO != null && redisLockBO.getCurrentTread().equals(currentThread)) {
                // 请求结束后，强制删掉 key，释放锁
                redisTemplate.delete(businessKey);
                // remove holder when release redis lock, Accept @zhuquanzhen Suggestion
                HOLDER_LIST.remove(redisLockBO);
                log.info("release the lock, businessKey is [" + businessKey + "]");
            }
        } catch (Exception e) {
            log.error("release the lock error", e);
        }
    }

    /**
     * 获取当前切点的方法
     * @param pjp 切点
     * @return Method
     */
    private Method resolveMethod(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Class<?> targetClass = pjp.getTarget().getClass();
        Method method = getDeclaredMethodFor(targetClass, signature.getName(), signature.getMethod().getParameterTypes());
        if (method == null) {
            throw new IllegalStateException("Cannot resolve target method: " + signature.getMethod().getName());
        }
        return method;
    }

    /**
     * 获取指定类上的指定方法
     * @param clazz          指定类
     * @param name           指定方法
     * @param parameterTypes 参数类型列表
     * @return 找到就返回method，否则返回null
     */
    public static Method getDeclaredMethodFor(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getDeclaredMethodFor(superClass, name, parameterTypes);
            }
        }
        return null;
    }
}