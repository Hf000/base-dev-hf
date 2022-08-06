package org.hf.boot.springboot.config;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.hf.boot.springboot.task.dynamicTask.ElasticJobListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:hufei
 * @CreateTime:2020-12-01
 * @Description:elastic-job配置类
 */
@Configuration
public class ElasticJobConfig {

    //配置文件中的zookeeper的ip和端口
    @Value(value = "${zk}")
    private String serverlists;
    //指定一个命名空间
    @Value("${namesp}")
    private String namespace;

    /***
     * 配置Zookeeper和namespace
     * @return
     */
    @Bean
    public ZookeeperConfiguration zkConfig() {
        return new ZookeeperConfiguration(serverlists, namespace);
    }

    /***
     * 向zookeeper注册初始化信息
     * @param config
     * @return
     */
    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter regCenter(ZookeeperConfiguration config) {
        return new ZookeeperRegistryCenter(config);
    }

    /****
     * 创建ElasticJob的监听器实例
     * @return
     */
    @Bean
    public ElasticJobListener elasticJobListener() {
        //初始化要给定超时多少秒重连
        return new ElasticJobListener(100L,100L);
    }
}
