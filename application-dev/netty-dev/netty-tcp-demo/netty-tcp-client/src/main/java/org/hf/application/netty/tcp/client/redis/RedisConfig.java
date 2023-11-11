package org.hf.application.netty.tcp.client.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

/**
 * spring基于redis的缓存配置
 * @author hf
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    private  static final String CACHE_PREFIX = "netty-tcp-custom";

    /**
     * 缓存时长
     */
    @Value("${spring.redis.ttl:120}")
    private Long cacheTtl;

    /**
     * redis序列化配置
     * @param connectionFactory 连接参数
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        FastJson2JsonRedisSerializer<Object> serializer = new FastJson2JsonRedisSerializer<>(Object.class);
        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new KeyStringRedisSerializer(CACHE_PREFIX));
        // 设置value的序列化和反序列化
        template.setValueSerializer(serializer);
        // Hash的key也采用StringRedisSerializer的序列化方式
        template.setHashKeySerializer(new KeyStringRedisSerializer(CACHE_PREFIX));
        template.setHashValueSerializer(serializer);
        // 初始化RedisTemplate完成序列化的方法
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 配置缓存管理器
     * @param connectionFactory 连接参数
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                //缓存时长
                .entryTtl(Duration.ofSeconds(cacheTtl))
                .disableCachingNullValues();
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(cacheConfiguration)
                .build();
    }
}