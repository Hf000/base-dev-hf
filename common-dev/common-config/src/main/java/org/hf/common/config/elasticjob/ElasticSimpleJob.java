package org.hf.common.config.elasticjob;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> elastic-job作业类注解 </p>
 * 自定义封装elastic-job实现 - 1
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/9 15:29
 */
@Inherited
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticSimpleJob {

    /**
     * cron表达式值   value和cron二选一必填
     * @return 返回表达式
     */
    @AliasFor("cron")
    String value() default "";

    /**
     * cron表达式    value和cron二选一必填
     * @return 返回表达式
     */
    @AliasFor("value")
    String cron() default "";

    /**
     * 定时任务名称
     * @return 返回名称
     */
    String jobName() default "";

    /**
     * 作业分片总数，默认为1
     * @return 返回分片数量
     */
    int shardingTotalCount() default 1;

    /**
     * 分片序列号和参数用等号分隔，多个键值对用逗号分隔
     * 分片序列号从0开始，不可大于或等于作业分片总数  0=a,1=b,2=c
     * @return 返回值
     */
    String shardingItemParameters() default "";

    /**
     * 作业自定义参数
     * @return 返回自定义参数
     */
    String jobParameter() default "";

    /**
     * 本地配置是否可覆盖注册中心配置
     * 如果可覆盖，每次启动作业都以本地配置为准
     * @return 是否覆盖
     */
    boolean overwrite() default true;

    /**
     * 是否开启失效转移 仅monitorExecution开启，失效转移才有效
     * @return 返回值
     */
    boolean failover() default false;

    /**
     * 作业是否禁止启动
     * @return 返回值
     */
    boolean disabled() default false;

    /**
     * 作业描述信息
     * @return 返回描述信息
     */
    String description() default "";

    /**
     * 作业分片策略实现类全路径
     * 默认使用平均分配策略
     * @return 返回
     */
    String jobShardingStrategyClass() default "";

}
