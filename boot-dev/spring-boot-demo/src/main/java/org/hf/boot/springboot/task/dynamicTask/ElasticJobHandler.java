package org.hf.boot.springboot.task.dynamicTask;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author:hufei
 * @CreateTime:2020-12-01
 * @Description:构建任务
 */
@Component
public class ElasticJobHandler {

    @Autowired
    private ZookeeperRegistryCenter registryCenter;

    @Autowired
    private ElasticJobListener elasticJobListener;

    /**
     * @param jobName:任务的命名空间
     * @param jobClass:执行的定时任务对象
     * @param shardingTotalCount：分片个数
     * @param cron：定时周期表达式
     * @param id：自定义参数
     * @return
     */
    private static LiteJobConfiguration.Builder simpleJobConfigBuilder(String jobName,
                                                                       Class<? extends SimpleJob> jobClass,
                                                                       int shardingTotalCount,
                                                                       String cron,
                                                                       String id) {
        //创建任务构建对象
        LiteJobConfiguration.Builder builder = LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(
                JobCoreConfiguration.
                        //任务命名空间名字、任务执行周期表达式、分片个数
                                newBuilder(jobName, cron, shardingTotalCount).
                        //自定义参数
                                jobParameter(id).
                        build(),
                jobClass.getCanonicalName()));
        //本地配置是否可覆盖注册中心配置
        builder.overwrite(true);
        return builder;
    }

    /**
     * 添加一个定时任务
     * @param cron:周期执行表达式
     * @param id:自定义参数
     * @param jobName:命名空间
     * @param instance:任务对象
     */
    public void addPublishJob(String cron,String id,String jobName,SimpleJob instance) {
        LiteJobConfiguration jobConfig = simpleJobConfigBuilder(
                jobName,
                instance.getClass(),
                1,
                cron,
                id).overwrite(true).build();
        //DynamicTask为具体的任务执行逻辑类
        new SpringJobScheduler(instance, registryCenter, jobConfig, elasticJobListener).init();
    }

    /***
     * Date转cron表达式
     */
    public static final String CRON_DATE_FORMAT = "ss mm HH dd MM ? yyyy";

    /**
     * 获得定时
     * @param date
     * @return
     */
    public static String getCron(final Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CRON_DATE_FORMAT);
        return simpleDateFormat.format(date);
    }
}
