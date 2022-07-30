package org.hf.common.config.elasticjob;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hf.common.config.enums.ConfigExceptionEnum;
import org.hf.common.publi.utils.ElUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;

/**
 * <p> elastic-job初始化处理 </p>
 * 自定义封装elastic-job实现 - 2
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/9 21:29
 */
@Slf4j
@Configuration
public class ElasticJobAutoConfiguration {

    @Autowired
    private ElasticJobProperties elasticJobProperties;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Environment environment;

    /**
     * elastic-job初始化处理
     */
    @PostConstruct
    public void initElasticJob() {
        log.info("定时任务初始化开始===> elasticJobProperties:{}", elasticJobProperties.toString());
        // 判断是否开启了定时任务开关
        if (elasticJobProperties.isOpen()) {
            // 初始化zookeeper配置
            ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(elasticJobProperties.getAddress(), elasticJobProperties.getNamespace());
            if (elasticJobProperties.isFlag()) {
                zookeeperConfiguration.setBaseSleepTimeMilliseconds(elasticJobProperties.getBaseSleepTimeMilliseconds());
                zookeeperConfiguration.setMaxSleepTimeMilliseconds(elasticJobProperties.getMaxSleepTimeMilliseconds());
                zookeeperConfiguration.setMaxRetries(elasticJobProperties.getMaxRetries());
            }
            // 初始注册中心,建立连接
            ZookeeperRegistryCenter zookeeperRegistryCenter = new ZookeeperRegistryCenter(zookeeperConfiguration);
            zookeeperRegistryCenter.init();
            // 获取spring容器中的任务类
            Map<String, SimpleJob> beansOfType = applicationContext.getBeansOfType(SimpleJob.class);
            for (Map.Entry<String, SimpleJob> simpleJobEntry : beansOfType.entrySet()) {
                log.info("获取实现simple接口的bean ===> {}", simpleJobEntry.getKey());
                // 获取当前类是否有定时任务注解
                SimpleJob simpleJob = simpleJobEntry.getValue();
                ElasticSimpleJob elasticSimpleJob = simpleJob.getClass().getAnnotation(ElasticSimpleJob.class);
                Optional.ofNullable(elasticSimpleJob).ifPresent(element ->{
                    log.info("获取添加了ElasticSimpleJob注解的bean ===> {}", simpleJob.getClass().getName());
                    String cron = StringUtils.defaultIfBlank(elasticSimpleJob.cron(), elasticSimpleJob.value());
                    // 判断是否是el表达式还是cron表达式
                    if (ElUtil.judgmentEl(cron)) {
                        // 使用spring环境properties解析后的上下文对象environment获取cron表达式对应的值
                        cron = environment.resolvePlaceholders(cron);
                    } else {
                        cron = ElUtil.replaceEl(cron);
                    }
                    if (StringUtils.isBlank(cron)) {
                        throw new ElasticJobException(ConfigExceptionEnum.CRON_ERROR);
                    }
                    String jobName = StringUtils.defaultIfBlank(elasticSimpleJob.jobName(), simpleJob.getClass().getName());
                    // 设置定时任务配置项
                    SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(JobCoreConfiguration.newBuilder(jobName, cron, elasticSimpleJob.shardingTotalCount())
                            .shardingItemParameters(elasticSimpleJob.shardingItemParameters()).jobParameter(elasticSimpleJob.jobParameter()).description(elasticSimpleJob.description())
                            .failover(elasticSimpleJob.failover()).build(), simpleJob.getClass().getCanonicalName());
                    LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(elasticSimpleJob.overwrite()).disabled(elasticSimpleJob.disabled())
                            .jobShardingStrategyClass(elasticSimpleJob.jobShardingStrategyClass()).build();
                    // 初始化任务
                    SpringJobScheduler springJobScheduler = new SpringJobScheduler(simpleJob, zookeeperRegistryCenter, liteJobConfiguration);
                    springJobScheduler.init();
                });
            }
        }
        log.info("定时任务初始化结束");
    }

}
