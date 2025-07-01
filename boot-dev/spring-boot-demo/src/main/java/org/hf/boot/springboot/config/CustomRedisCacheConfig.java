package org.hf.boot.springboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.BatchStrategies;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> 缓存配置,基于redis缓存 </p >
 * 1.原理: Spring Cache的缓存实现是通过AOP进行拦截生成代理对象对目标方法进行增强, 在redis中的存储是以命名空间的方式进行逻辑隔离的
 * 2.使用方法: 在对应的方法上加上此注解@Cacheable(cacheNames = "ONE_MIN_CACHE"),cacheNames表示这一块的缓存都用同样的缓存
 *  配置,具体是哪个缓存通过key区分
 * 3.注意: 被@Cacheable修饰的方法需要被AOP代理, 否则会失效
 * 4.缓存删除避坑: 如果在进行缓存key的设置时,调用了disableKeyPrefix()方法进行了缓存key的前缀禁用,此时在redis中生成的命名空间和key
 *  是不会拼接cacheNames的值的; 如果通过@CacheEvict执行缓存清除且不指定key且allEntries=true的情况下, 会先通过keys命令匹配对应键,
 *  如果cacheNames配置错误或为空则会导致匹配到所有的缓存key, 类似于执行"keys *" 指令, 此外如果在进行Spring Cache集成Redis配置时,
 *  进行key的配置设置key的前缀禁用时, 也就是调用了disableKeyPrefix(), 也会导致匹配所有缓存key, 所以就会清除redis下的所有缓存
 *
 * @author HF
 * @date 2023-05-12
 **/
@Slf4j
@Configuration
@EnableCaching
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnProperty(name = "redis.cache.enable", havingValue = "true")
public class CustomRedisCacheConfig extends CachingConfigurerSupport {

    public CustomRedisCacheConfig() {
        log.info("初始化自定义Redis缓存配置...");
    }

    /**
     * 缓存管理器, 此处报红是由于springboot版本高导致的, 换成2.6.x及以下就ok, 也并不影响启动和编译
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheManager redisCacheManager = RedisCacheManager
                // 这样配置扫描是通过keys命令进行缓存key扫描
//                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                // 这样配置扫描是通过scan命令进行缓存key扫描
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory, BatchStrategies.scan(1000)))
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
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                /** 注意: 如果在进行缓存key的设置时,调用了disableKeyPrefix()方法进行了缓存key的前缀禁用,此时在redis中生成的命名空间和key
                 *  是不会拼接cacheNames的值的; 如果通过@CacheEvict执行缓存清除且不指定key且allEntries=true的情况下, 会先通过keys命令
                 *  匹配对应键, 如果cacheNames配置错误或为空则会导致匹配到所有的缓存key, 类似于执行"keys *" 指令, 此外如果在进行
                 *  Spring Cache集成Redis配置时, 进行key的配置设置key的前缀禁用时, 也就是调用了disableKeyPrefix(), 也会导致匹配所有
                 *  缓存key, 所以就会清除redis下的所有缓存
                 **/
//                .disableKeyPrefix()
                ;
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
        /** 注意: 如果在进行缓存key的设置时,调用了disableKeyPrefix()方法进行了缓存key的前缀禁用,此时在redis中生成的命名空间和key
         *  是不会拼接cacheNames的值的; 如果通过@CacheEvict执行缓存清除且不指定key且allEntries=true的情况下, 会先通过keys命令
         *  匹配对应键, 如果cacheNames配置错误或为空则会导致匹配到所有的缓存key, 类似于执行"keys *" 指令, 此外如果在进行
         *  Spring Cache集成Redis配置时, 进行key的配置设置key的前缀禁用时, 也就是调用了disableKeyPrefix(), 也会导致匹配所有
         *  缓存key, 所以就会清除redis下的所有缓存
         **/
//        customizedRedisCacheConfigurationMap.put("HALF_HOUR_CACHE", getRedisCacheConfigurationWithTtl(1800).disableKeyPrefix());
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
     * 基于redis+lua限流实现注解 - 3
     * @param redisConnectionFactory redis连接工厂
     * @return org.springframework.data.redis.core.RedisTemplate<String, Object>
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //设置key的序列化方式为String
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // GenericJackson2JsonRedisSerializer可以替换为Jackson2JsonRedisSerializer(Object.class)
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

    /**
     * redis配合使用lua脚本配置
     * 基于redis+lua限流实现注解 - 2
     */
    @Bean
    public DefaultRedisScript<Number> redisLuaScript() {
        DefaultRedisScript<Number> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redisLimit.lua")));
        redisScript.setResultType(Number.class);
        return redisScript;
    }
}
