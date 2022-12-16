package org.hf.common.publi.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.HashUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.hf.common.publi.constants.BussinessCodeEnum;
import org.hf.common.publi.exception.BusinessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.hf.common.publi.constants.DateFormatConstant.YY_MM_DD_FORMAT;
import static org.hf.common.publi.constants.DateFormatConstant.YY_MM_DD_HH_FORMAT;
import static org.hf.common.publi.constants.DateFormatConstant.YY_MM_DD_HH_MM_FORMAT;

/**
 * <p> 统一序列编号生成工具类 </p >
 * 支持并发生成，保证惟一
 * 支持生成三种序列号规则：短（万条/天）、中(十万条/小时)、长(百万条/分钟)，根据业务场景需要选择使用
 *
 * @author HF
 * @date 2022-11-28
 **/
@Slf4j
public class SerialNoUtil {

    private static final String REDIS_KEY_PREFIX = "-serialNo-";

    /**
     * 支持万条/天
     * 规则：
     * 1）总长度：26位起
     * 2）业务编码（2位） + 机器码（2位） + 时间（YYMMDD，6位） + 4位自增数起步 + 随机码（8位）
     * 3）使用该规则，建议字段长度：varchar(26)
     *
     * @param bussinessCodeEnum 业务编码枚举
     * @return String
     */
    public static String genShortSerialNo(BussinessCodeEnum bussinessCodeEnum) {
        if (bussinessCodeEnum == null) {
            throw new BusinessException("业务编码不能为空");
        }
        try {
            String businessCode = bussinessCodeEnum.getCode().toUpperCase();
            String redisKey = businessCode + REDIS_KEY_PREFIX + "16d";
            StringBuilder serialNo = new StringBuilder();
            // 业务编码（2位）
            serialNo.append(businessCode);
            // 机器ip hash（2位）
            serialNo.append(genMachineIpHash());
            // 时间（YYMMDD，6位）
            serialNo.append(DateUtil.format(new Date(), YY_MM_DD_FORMAT));
            // 自增数(>=4位)
            serialNo.append(formatAutoIncrNo(genCurrYyMmDdExpireAutoIncrNo(redisKey), 4));
            // 随机码（8位）
            serialNo.append(genRandomInt());
            log.info("获取统一序列编号，redisKey={}，serialNo={}", redisKey, serialNo);
            return serialNo.toString();
        } catch (Exception e) {
            log.error("获取统一序列编号，genShortSerialNo16d出错，cause：", e);
            throw new BusinessException("获取统一序列编号出错，请检查！");
        }
    }

    /**
     * 支持十万条/小时
     * 规则：
     * 1）总长度：30位起
     * 2）业务编码（2位） + 机器码（2位） + 时间（YYMMDDHH，8位） + 5位自增数起步 + 随机码（8位）
     * 3）使用该规则，建议字段长度：varchar(30)
     *
     * @param bussinessCodeEnum 业务编码
     * @return String
     */
    public static String genMiddleSerialNo(BussinessCodeEnum bussinessCodeEnum) {
        if (bussinessCodeEnum == null) {
            throw new BusinessException("业务编码不能为空");
        }
        try {
            String businessCode = bussinessCodeEnum.getCode().toUpperCase();
            String redisKey = businessCode + REDIS_KEY_PREFIX + "20d";
            StringBuilder serialNo = new StringBuilder();
            // 业务编码（2位）
            serialNo.append(businessCode);
            // 机器ip hash（2位）
            serialNo.append(genMachineIpHash());
            // 时间（YYMMDDHH，8位）
            serialNo.append(DateUtil.format(new Date(), YY_MM_DD_HH_FORMAT));
            // 自增数(>=5位)
            serialNo.append(formatAutoIncrNo(genCurrYyMmDdHhExpireAutoIncrNo(redisKey), 5));
            // 随机码（8位）
            serialNo.append(genRandomInt());
            log.info("获取统一序列编号，redisKey={}，serialNo={}", redisKey, serialNo);
            return serialNo.toString();
        } catch (Exception e) {
            log.error("获取统一序列编号，genMiddleSerialNo20d出错，cause：", e);
            throw new BusinessException("获取统一序列编号出错，请检查！");
        }
    }

    /**
     * 支持百万条/分钟
     * 规则：
     * 1）总长度：32位起
     * 2）业务编码（2位） + 机器码（2位） + 时间（YYMMDDHHMI，10位） + 6位自增数起步 + 随机码（8位）
     * 3）使用该规则，建议字段长度：varchar(32)
     *
     * @param bussinessCodeEnum 业务编码
     * @return String
     */
    public static String genLongSerialNo(BussinessCodeEnum bussinessCodeEnum) {
        if (bussinessCodeEnum == null) {
            throw new BusinessException("业务编码不能为空");
        }
        try {
            String businessCode = bussinessCodeEnum.getCode().toUpperCase();
            String redisKey = businessCode + REDIS_KEY_PREFIX + "24d";
            StringBuilder serialNo = new StringBuilder();
            //业务编码（2位）
            serialNo.append(businessCode);
            // 机器ip hash（2位）
            serialNo.append(genMachineIpHash());
            // 时间（yyMMddHHmm，10位）
            serialNo.append(DateUtil.format(new Date(), YY_MM_DD_HH_MM_FORMAT));
            // 自增数(>=6位)
            serialNo.append(formatAutoIncrNo(genCurrYyMmDdHhMiExpireAutoIncrNo(redisKey), 6));
            // 随机码（4位）
            serialNo.append(genRandomInt());
            log.info("获取统一序列编号，redisKey={}，serialNo={}", redisKey, serialNo);
            return serialNo.toString();
        } catch (Exception e) {
            log.error("获取统一序列编号，genLongSerialNo24d出错，cause：", e);
            throw new BusinessException("获取统一序列编号出错，请检查！");
        }
    }

    /**
     * 获取机器IP的hash值，长度2位
     *
     * @return String
     * @throws Exception 异常
     */
    private static String genMachineIpHash() throws Exception {
        return StringUtils.leftPad("" + HashUtil.additiveHash(LocalIpUtils.getLocalIp(), 97), 2, "0");
    }

    /**
     * 获取随机码，长度8位
     *
     * @return String
     */
    private static String genRandomInt() {
        return StringUtils.leftPad("" + RandomUtils.nextInt(0, 100000000), 8, "0");
    }

    /**
     * 格式化自增数
     *
     * @param serialNo 序列号
     * @param length   长度
     * @return String
     */
    private static String formatAutoIncrNo(long serialNo, int length) {
        return StringUtils.leftPad("" + serialNo, length, "0");
    }

    /**
     * 获取自增数，redis锁24小时有效期<br/>
     *
     * @param redisKey 入参
     * @return long
     */
    private static long genCurrYyMmDdExpireAutoIncrNo(String redisKey) {
        RedisTemplate<String, String> redisTemplate = SpringBeanUtil.getBean(StringRedisTemplate.class);
        RedisAtomicLong counter = new RedisAtomicLong(redisKey, Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        long num = counter.incrementAndGet();
        counter.expire(24, TimeUnit.HOURS);
        return num;
    }

    /**
     * 获取自增数，redis锁60分钟有效期<br/>
     *
     * @param redisKey 入参
     * @return long
     */
    private static long genCurrYyMmDdHhExpireAutoIncrNo(String redisKey) {
        RedisTemplate<String, String> redisTemplate = SpringBeanUtil.getBean(StringRedisTemplate.class);
        RedisAtomicLong counter = new RedisAtomicLong(redisKey, Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        long num = counter.incrementAndGet();
        counter.expire(60, TimeUnit.MINUTES);
        return num;
    }

    /**
     * 获取自增数，redis锁60秒有效期<br/>
     *
     * @param redisKey 入参
     * @return long
     */
    private static long genCurrYyMmDdHhMiExpireAutoIncrNo(String redisKey) {
        RedisTemplate<String, String> redisTemplate = SpringBeanUtil.getBean(StringRedisTemplate.class);
        RedisAtomicLong counter = new RedisAtomicLong(redisKey, Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        long num = counter.incrementAndGet();
        counter.expire(60, TimeUnit.SECONDS);
        return num;
    }
}