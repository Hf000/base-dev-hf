package org.hf.boot.springboot.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> redis锁 </p >
 * 1.redis锁注解, 切面: org.hf.boot.springboot.aop.PrefixRedisLockAspect
 * 通过lockNamePrefix（锁前缀）+ lockNameDiff（锁的特殊标识符）/lockNameDiffExpress（锁的特殊标识符SpEL表达式，支持从方法入参或当前对象中获取属性作为锁的特殊标识，也支持其他SpEL的使用，例如从UserContext中获取userId）锁定expiredInMilliSeconds（锁存活时长）毫秒。且在获取不到锁时，会在maxWaitTimeInMilliSeconds内进行重试获取锁。
 * 用法:
 *  1> userId作为固定值
 *     @CustomPrefixRedisLock(lockNamePrefix = "UPDATE_USER_INFO_LOCK_PREFIX", lockNameDiff = "1234")
 *     public void updateUserInfo() {...}
 *  2> userId作为当前对象的成员变量
 *     @Getter
 *     private Integer userId;
 *     @CustomPrefixRedisLock(lockNamePrefix = "UPDATE_USER_INFO_LOCK_PREFIX", lockNameDiffExpress = "#{this.userId}")
 *     public void updateUserInfo() {...}
 *  3> userId作为方法入参
 *     @CustomPrefixRedisLock(lockNamePrefix = "UPDATE_USER_INFO_LOCK_PREFIX", lockNameDiffExpress = "#{userId}")
 *     public void updateUserInfo(Integer userId) {...}
 *  4> userId作为方法入参的属性
 *     @CustomPrefixRedisLock(lockNamePrefix = "UPDATE_USER_INFO_LOCK_PREFIX", lockNameDiffExpress = "#{userInfo.userId}")
 *     public void updateUserInfo(UserInfo userInfo) {...}
 *  5> userId从静态方法中获取
 *     @CustomPrefixRedisLock(lockNamePrefix = "UPDATE_USER_INFO_LOCK_PREFIX", lockNameDiffExpress = "#{T(org.hf.boot.springboot.config.UserContext).getUserId()}")
 *     public void updateUserInfo() {...}
 *  6> 分布式锁工具类：org.hf.boot.springboot.aop.PrefixRedisLockUtil
 *     public class Test {
 *         @Autowired
 *         private PrefixRedisLockUtil lockUtils;
 *         public void test() {
 *             String lockName = "TEST";
 *             String reds = lockUtils.lock(this::testLock, lockName);
 *         }
 *         private String testLock() {
 *         ...
 *         }
 *    }
 *  自定义redis锁CustomPrefixRedisLock实现 - 1
 * @author hufei
 * @date 2023-04-11
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomPrefixRedisLock {

    /**
     * 锁前缀
     */
    String lockNamePrefix();

    /**
     * 锁特殊标识符的SpEL表达式，例如通过userId使用更小粒度的锁
     */
    String lockNameDiffExpress() default "";

    /**
     * (支持多重锁的)锁特殊标识符的表达式,当需要多个锁的场景时可以使用。会默认用String的自然顺序排序后逐个获取锁
     */
    String multiLockNameDiffExpress() default "";

    /**
     * 锁特殊标识符值，传入固定值可以直接和lockNamePrefix拼接成小粒度的锁
     */
    String lockNameDiff() default "";

    /**
     * 锁的requestId, 默认自动生成
     */
    String requestId() default "";

    /**
     * 锁存活时长，设置为0时 默认存活10s
     */
    long expiredInMilliSeconds() default 0;

    /**
     * 获取不到锁的最长等待重试时长，默认不等待
     */
    long maxWaitTimeInMilliSeconds() default 0;
}