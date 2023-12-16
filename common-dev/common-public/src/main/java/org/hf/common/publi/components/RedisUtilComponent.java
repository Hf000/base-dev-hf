package org.hf.common.publi.components;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.hf.common.publi.utils.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p> Redis工具处理类 </p>
 * @author HUFEI
 * @date 2023-05-09
 **/
@Slf4j
@Component
@ConditionalOnClass(StringRedisTemplate.class)
public class RedisUtilComponent {

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplateObj;

    /**
     * 设置字符串
     * @param key   key
     * @param value value
     */
    public void setString(String key, String value) {
        log.debug("RedisUtilComponent.setString Key:{}, Value:{}", key, value);
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置字符串, 带过期时间
     * @param key       key
     * @param value     value
     * @param minutes   key失效时间, 单位分钟
     */
    public void setStringWithMinutes(String key, String value, int minutes) {
        log.debug("RedisUtilComponent.setStringWithMinutes Key:{}, Value:{}, minutes:{}", key, value, minutes);
        redisTemplate.opsForValue().set(key, value, minutes, TimeUnit.MINUTES);
    }

    /**
     * 设置字符串, 带过期时间
     * @param key       key
     * @param value     value
     * @param seconds   key失效时间, 单位秒
     */
    public void setStringWithSeconds(String key, String value, int seconds) {
        log.debug("RedisUtilComponent.setStringWithMinutes Key:{}, Value:{}, seconds:{}", key, value, seconds);
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 设置对象 将对象转换成json字符串
     * @param key       key
     * @param value     value
     */
    public void setObject(String key, Object value) {
        String valueJson = "";
        if (value != null) {
            valueJson = JSON.toJSONString(value);
        }
        log.debug("RedisUtilComponent.setObject Key:{}, Value:{}", key, valueJson);
        redisTemplate.opsForValue().set(key, valueJson);
    }

    /**
     * 设置对象 将对象转换成json字符串
     * @param key       key
     * @param value     value
     * @param minutes   key失效时间 单位: 分钟
     */
    public void setObjectWithMinutes(String key, Object value, int minutes) {
        String valueJson = "";
        if (value != null) {
            valueJson = JSON.toJSONString(value);
        }
        log.debug("RedisUtilComponent.setObject Key:{}, Value:{}, minutes:{}", key, valueJson, minutes);
        redisTemplate.opsForValue().set(key, valueJson, minutes, TimeUnit.MINUTES);
    }

    /**
     * 设置对象 将对象转换成json字符串, 存在则不set, 不存在则set
     * @param key       key
     * @param value     value
     * @param minutes   key失效时间 单位: 分钟
     * @return Boolean true-存在, false-不存在
     */
    public Boolean setObjectIfAbsentWithMinutes(String key, Object value, int minutes) {
        String valueJson = "";
        if (value != null) {
            valueJson = JSON.toJSONString(value);
        }
        log.debug("RedisUtilComponent.setObjectIfAbsentWithMinutes Key:{}, Value:{}, minutes:{}", key, valueJson, minutes);
        return redisTemplate.opsForValue().setIfAbsent(key, valueJson, minutes, TimeUnit.MINUTES);
    }

    /**
     * 设置List, 将对象转换成json字符串
     * @param key       key
     * @param value     value
     * @param minutes   key失效时间, 单位:分钟
     */
    public void setListWithMinutes(String key, List<Object> value, int minutes) {
        String valueJson = "";
        if (value != null) {
            valueJson = JSON.toJSONString(value);
        }
        log.debug("RedisUtilComponent.setObject Key:{}, Value:{}, minutes:{}", key, valueJson, minutes);
        redisTemplate.opsForValue().set(key, valueJson, minutes, TimeUnit.MINUTES);
    }

    /**
     * 设置对象 将对象转换成json字符串
     * @param key       key
     * @param value     value
     * @param seconds   key失效时间 单位: 秒
     */
    public void setObjectWithSeconds(String key, Object value, int seconds) {
        String valueJson = "";
        if (value != null) {
            valueJson = JSON.toJSONString(value);
        }
        log.debug("RedisUtilComponent.setObject Key:{}, Value:{}, seconds:{}", key, valueJson, seconds);
        redisTemplate.opsForValue().set(key, valueJson, seconds, TimeUnit.SECONDS);
    }

    /**
     * 获取key对应的value
     * @param key key
     * @return String
     */
    public String getString(String key) {
        if (key == null || key.length() == 0) {
            return null;
        }
        String resultStr = redisTemplate.opsForValue().get(key);
        log.debug("RedisUtilComponent.getString Key:{}, Value:{}", key, resultStr);
        return resultStr;
    }

    /**
     * 获取key对应的value, 将json字符串转换成指定对象
     * @param key   key
     * @param clazz 类型对象
     * @param <T>   对象类型
     * @return T
     */
    public <T> T getObject(String key, Class<T> clazz) {
        if (key == null || key.length() == 0) {
            return null;
        }
        String stringValue = redisTemplate.opsForValue().get(key);
        log.debug("RedisUtilComponent.getObject Key:{}, Value:{}", key, stringValue);
        return JSON.parseObject(stringValue, clazz);
    }

    /**
     * 获取key对应的value, 将json字符串转换成指定的集合对象
     * @param key   key
     * @param clazz 类型对象
     * @param <T>   对象类型
     * @return List<T>
     */
    public <T> List<T> getJsonArray(String key, Class<T> clazz) {
        if (key == null || key.length() == 0) {
            return Collections.emptyList();
        }
        String stringValue = redisTemplate.opsForValue().get(key);
        log.debug("RedisUtilComponent.getObject Key:{}, Value:{}", key, stringValue);
        return JSON.parseArray(stringValue, clazz);
    }

    /**
     * 异常指定key
     * @param key key
     */
    public void remove(String key) {
        log.debug("RedisUtilComponent.remove Key:{}", key);
        redisTemplate.delete(key);
    }

    /**
     * 设置key的过期时间, 单位:秒
     * @param key key
     * @param i   过期时间 单位:秒
     */
    public void expireBySeconds(String key, int i) {
        redisTemplate.expire(key, i, TimeUnit.SECONDS);
    }

    /**
     * 设置key的过期时间, 单位:分钟
     * @param key key
     * @param i   过期时间 单位:分钟
     */
    public void expireByMinutes(String key, int i) {
        redisTemplate.expire(key, i, TimeUnit.MINUTES);
    }

    /**
     * 设置set类型的值
     * @param key       key
     * @param values    value集合
     */
    public void addSet(String key, List<Object> values) {
        SetOperations<String, Object> redisSet = redisTemplateObj.opsForSet();
        redisSet.add(key, values.toArray());
    }

    /**
     * 设置set类型的值
     * @param key       key
     * @param values    value集合
     * @param time      key过期时间
     * @param timeUnit  过期时间单位
     */
    public void addSetWithTimeUnit(String key, List<Object> values, long time, TimeUnit timeUnit) {
        SetOperations<String, Object> redisSet = redisTemplateObj.opsForSet();
        redisSet.add(key, values.toArray());
        redisTemplateObj.expire(key, time, timeUnit);
    }

    /**
     * 从左放入值到list类型数据中
     * @param key       key
     * @param value     value
     */
    public void leftPush(String key, String value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 从右边取值list类型数据值
     * @param key   key
     */
    public String rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 获取指定key的过期时间, 单位: 秒
     * @param key key
     * @return 过期时间 单位:秒
     */
    public Long getExpireBySeconds(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 获取指定key的过期时间, 单位: 分钟
     * @param key key
     * @return 过期时间 单位:分钟
     */
    public Long getExpireByMinutes(String key) {
        return redisTemplate.getExpire(key, TimeUnit.MINUTES);
    }

    /**
     * 判断key是否存在
     * @param key key
     * @return Boolean
     */
    public Boolean isExists(String key) {
        Boolean flag = redisTemplate.hasKey(key);
        return flag != null ? flag : Boolean.FALSE;
    }

    /**
     * 对绑定key的值进行自增, 线程安全, 绑定值类型要long类型
     * @param key       key
     * @param value     自增大小, long类型
     * @return 自增后返回值Long
     */
    public Long incrBy(String key, long value) {
        return redisTemplate.boundValueOps(key).increment(value);
    }

    /**
     * Redis Incr 命令将 key 中储存的数字值增1，如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作，且将key的有效时间设置为长期有效。
     * @param key key
     * @return 自增后结果
     */
    public Long incr(String key) {
        return redisTemplate.boundValueOps(key).increment(1);
    }

    /**
     * Redis Incr 命令将 key 中储存的数字值减1
     * @param key key
     * @return 递减后结果
     */
    public Long decr(String key) {
        return redisTemplate.boundValueOps(key).decrement(1);
    }

    /**
     * 指定时间内自增
     * @param key     键值
     * @param minutes 过期期限，单位：分钟
     * @return 自增结果，首次自增返回1
     */
    public Long incrExpireMin(String key, int minutes) {
        Long increment = redisTemplate.boundValueOps(key).increment(1);
        if (increment != null && increment.equals(1L)) {
            redisTemplate.expire(key, minutes, TimeUnit.MINUTES);
        }
        return increment;
    }

    /**
     * 指定时间内自增
     * @param key     键值
     * @param timeout 过期期限
     * @param unit    时间单位
     * @return 自增结果，首次自增返回1
     */
    public Long incrExpireTime(String key, int timeout, TimeUnit unit) {
        Long increment = redisTemplate.boundValueOps(key).increment(1);
        if (increment != null && increment.equals(1L)) {
            redisTemplate.expire(key, timeout, unit);
        }
        return increment;
    }

    /**
     * 查询所有key,如 a*   不推荐使用
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * redis-cli> hget key hashKey 获取hash类型数据key对应的键值对数据
     */
    public Object hGet(String key, String hashKey) {
        log.debug("RedisUtilComponent.hGet Key:{}, hashKey:{}", key, hashKey);
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * redis-cli> hset key hashKey val  设置hash类型数据key对应的键值对数据
     */
    public Boolean hSet(String key, String hashKey, String val) {
        log.debug("RedisUtilComponent.hSet Key:{}, hashKey:{}, val:{}", key, hashKey, val);
        try {
            redisTemplate.opsForHash().put(key, hashKey, val);
            return true;
        } catch (Exception e) {
            log.error("RedisUtilComponent.hSet ERROR, e:{}, Key:{}, hashKey:{}, val:{}", e, key, hashKey, val);
            return false;
        }
    }

    /**
     * redis-cli> hincrby key hashKey by  对hash类型数据对应key的键值对的值进行自定义自增大小
     * @param key       key
     * @param hashKey   hashKey
     * @param by        自增长度
     * @return 自增后返回的值
     */
    public Long hIncr(String key, String hashKey, Long by) {
        return redisTemplate.opsForHash().increment(key, hashKey, by);
    }

    /**
     * redis-cli> hscan key 0 match pattern 获取指定key下匹配指定键的键值对
     */
    public Map<String, Object> hScan(String key, String pattern) {
        Map<String, Object> res = new HashMap<>();
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(key, ScanOptions.scanOptions().match(pattern).build());
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> data = cursor.next();
            res.put(data.getKey().toString(), data.getValue());
        }
        return res;
    }

    /**
     * 删除指定前缀的key集合
     * @param prefix key前缀
     */
    public void deleteByPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix);
        if(keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 获取自增的redis对象
     * @param redisKey      key
     * @param initialValue  初始值
     * @return RedisAtomicLong
     */
    public RedisAtomicLong getRedisCounter(String redisKey, Long initialValue) {
        if (initialValue == null) {
            initialValue = 0L;
        }
        RedisTemplate<String, String> redisTemplate = SpringBeanUtil.getBean(StringRedisTemplate.class);
        return new RedisAtomicLong(redisKey, Objects.requireNonNull(redisTemplate.getConnectionFactory()), initialValue);
    }

    private final String LOCK_PRE = "dis_lock_";

    /**
     * 加锁
     */
    public Boolean lock(String key, long expireTime) {
        String value = LocalDateTime.now().toString();
        Expiration expiration = Expiration.seconds(expireTime);
        RedisStringCommands.SetOption option = RedisStringCommands.SetOption.ifAbsent();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        return redisTemplate.execute(connection -> connection.set(Objects.requireNonNull(stringRedisSerializer.serialize(LOCK_PRE + key)),
                Objects.requireNonNull(stringRedisSerializer.serialize(value)), expiration, option), true);
    }

    /**
     * 释放锁
     */
    public void unLock(String key) {
        key = LOCK_PRE + key;
        redisTemplate.delete(key);
    }

    /**
     * 设置锁时间
     */
    public void setLockTime(String key, long expireTime) {
        key = LOCK_PRE + key;
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }
}