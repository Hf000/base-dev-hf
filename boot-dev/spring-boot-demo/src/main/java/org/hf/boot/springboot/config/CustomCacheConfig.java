package org.hf.boot.springboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> 缓存配置,基于redis缓存 </p>
 * 使用方法, 在对应的方法上加上此注解@Cacheable(cacheNames = "ONE_MIN_CACHE"),cacheNames这表示这一块的缓存都用同样的缓存配置,具体是哪个缓存通过key区分
 * 注意: 被@Cacheable修饰的方法需要被AOP代理, 否则会失效
 * @author HUFEI
 * @date 2023-05-12
 **/
@Slf4j
@Configuration
@EnableCaching
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class CustomCacheConfig extends CachingConfigurerSupport {

    public CustomCacheConfig() {
        log.info("初始化自定义Redis缓存配置...");
    }

    /**
     * 缓存管理器, 此处报红是由于springboot版本高导致的, 换成2.6.x及以下就ok, 也并不影响启动和编译
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheManager redisCacheManager = RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                // 默认缓存配置,600s过期
                .cacheDefaults(getRedisCacheConfigurationWithTtl(600))
                // 针对不同的cacheNames指定不同的缓存配置
                .withInitialCacheConfigurations(customizedRedisCacheConfiguration())
                .build();
        log.info("redisCacheManager配置完成");
        return redisCacheManager;
    }

    /**
     * redis缓存配置
     */
    private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(long seconds) {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                /*key前缀 分号在RedisDesktopManager中可将key按照缓存空间来方便进行管理*/
                .computePrefixWith(cacheName -> cacheName + ":")
                /*当value为null时不缓存*/
                //.disableCachingNullValues()
                /*过期时间*/
                .entryTtl(Duration.ofSeconds(seconds))
                /*设置key序列化采用String的序列化方式*/
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                /*设置value序列化采用Jackson序列化*/
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    /**
     * 自定义一定时间的redis缓存配置
     * 为cacheName指定专属的缓存配置
     */
    private Map<String, RedisCacheConfiguration> customizedRedisCacheConfiguration() {
        Map<String, RedisCacheConfiguration> customizedRedisCacheConfigurationMap = new HashMap<>();
        customizedRedisCacheConfigurationMap.put("ONE_MIN_CACHE", getRedisCacheConfigurationWithTtl(60));
        customizedRedisCacheConfigurationMap.put("TWO_MIN_CACHE", getRedisCacheConfigurationWithTtl(120));
        customizedRedisCacheConfigurationMap.put("THREE_MIN_CACHE", getRedisCacheConfigurationWithTtl(180));
        customizedRedisCacheConfigurationMap.put("FIVE_MIN_CACHE", getRedisCacheConfigurationWithTtl(300));
        customizedRedisCacheConfigurationMap.put("TEN_MIN_CACHE", getRedisCacheConfigurationWithTtl(600));
        customizedRedisCacheConfigurationMap.put("FIFTEEN_MIN_CACHE", getRedisCacheConfigurationWithTtl(900));
        customizedRedisCacheConfigurationMap.put("TWENTY_MIN_CACHE", getRedisCacheConfigurationWithTtl(1200));
        customizedRedisCacheConfigurationMap.put("HALF_HOUR_CACHE", getRedisCacheConfigurationWithTtl(1800));
        return customizedRedisCacheConfigurationMap;
    }

    /**
     * 默认key生成策略
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) ->
                target.getClass().getSimpleName() + ":" +
                        method.getName() + "->" +
                        StringUtils.arrayToDelimitedString(params, "->").replaceAll("\\s", "");
    }

    /**
     * Redis模板自定义配置, 此处报红是由于springboot版本高导致的, 换成2.6.x及以下就ok, 也并不影响启动和编译
     * @param redisConnectionFactory redis连接工厂
     * @return org.springframework.data.redis.core.RedisTemplate<String, Object>
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        /* Hash key序列化 */
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        /* Hash value序列化 */
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    /**
     * redis的String类型模板配置, 此处报红是由于springboot版本高导致的, 换成2.6.x及以下就ok, 也并不影响启动和编译
     * @param redisConnectionFactory redis连接配置
     * @return StringRedisTemplate
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringRedisTemplate;
    }
}
