package org.hf.boot.springboot.custom.lock.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注意这里需要单独配置一下redisson的配置, 以免和redis的配置冲突
 */
@Configuration
@ConditionalOnProperty(name = "redis.enable", havingValue = "true")
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // 这里假设你使用单节点的Redis服务器
        config.useSingleServer()
            // 使用与Spring Data Redis相同的地址
            .setAddress("redis://127.0.0.1:6379");
        // 如果有密码
        //.setPassword("xxxx");
        // 其他配置参数
        //.setDatabase(0)
        //.setConnectionPoolSize(10)
        //.setConnectionMinimumIdleSize(2);
        // 创建RedissonClient实例
        return Redisson.create(config);
    }
}