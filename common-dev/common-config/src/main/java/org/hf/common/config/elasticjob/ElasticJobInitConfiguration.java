package org.hf.common.config.elasticjob;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import lombok.extern.slf4j.Slf4j;
import org.hf.common.config.enums.ConfigExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> elastic-job初始化处理 </p>
 * 自定义封装elastic-job实现 - 4
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/9 21:29
 */
@Slf4j
@Configuration
public class ElasticJobInitConfiguration {

    @Autowired
    private ElasticJobProperties elasticJobProperties;

    @Bean
    public ZookeeperRegistryCenter initZookeeperRegistryCenter() {
        try {
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
                return zookeeperRegistryCenter;
            }
        } catch (Exception e) {
            throw new ElasticJobException(ConfigExceptionEnum.JOB_CONFIG_ERROR);
        }
        return null;
    }

}
