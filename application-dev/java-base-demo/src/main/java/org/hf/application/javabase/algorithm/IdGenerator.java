package org.hf.application.javabase.algorithm;

import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * <p> id唯一生成 </p>
 * @author HUFEI
 * @date 2023-05-09
 **/
public class IdGenerator {

    /**
     * |------------------------------------64----------------------------------
     * |
     * |-retained--|-----41----|---------------13------------------|------9-----
     * |
     * |-retained--|----ms-----|--------------workerId-------------|--sequence--
     * |
     * |-----1-----|-----41----|-----4-----|-----2-----|-----7-----|------9-----
     * |
     * |-retained--|----ms-----|-business--|-position--|machine_id-|--sequence--
     * |
     */
    private static final long TIME_OFFSET;
    private static final long SEQUENCE_BITS = 9L;
    private static final long WORKER_ID_BITS = 13L;
    /**
     * 2^9 - 1
     */
    private static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;
    private static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;
    /**
     * 2^13
     */
    private static final long WORKER_ID_MAX_VALUE = 1L << WORKER_ID_BITS;
    /**
     * 默认分隔符，用于连接prefix和分布式id
     */

    private static final String DEFAULT_SEPARATOR = "_";

    static {
        // 2019-02-01
        LocalDateTime localDateTime = LocalDateTime.of(2019, 2, 1, 0, 0, 0);
        ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(localDateTime);
        TIME_OFFSET = localDateTime.toInstant(zoneOffset).toEpochMilli();
    }

    private final long workerId;
    private long sequence;
    private long lastTime;
    private volatile static IdGenerator instance = null;
    private StringBuilder sb;

    private IdGenerator(long workerId) {
        this.workerId = workerId;
    }

    /**
     * 获取Id生成器实例对象 (修改为双重锁形式，提升性能)
     * @param workerId 工作id-用于区分不同业务
     * @return IdGenerator
     */
    public static IdGenerator getInstance(long workerId) {
        if (workerId < 0 || workerId > WORKER_ID_MAX_VALUE) {
            throw new IllegalArgumentException("Invalid workerId<" + workerId + "> maxWorkerId<" + WORKER_ID_MAX_VALUE + ">");
        }
        if (instance == null) {
            synchronized (IdGenerator.class){
                if(instance == null){
                    instance = new IdGenerator(workerId);
                }
            }
        }
        return instance;
    }

    /**
     * 获取Id生成器实例对象 (根据机器本身的ip区分)
     * @return IdGenerator
     */
    public static synchronized IdGenerator getInstance(){
        long workId = getWorkId();
        return getInstance(workId);
    }

    /**
     * 获取生成的id
     * @return long
     */
    public synchronized long generateId() {
        long time = System.currentTimeMillis();
        if (lastTime > time) {
            throw new IllegalStateException("Clock is moving backwards, last time is " + lastTime + " milliseconds, current time is " + time + " milliseconds");
        }
        if (lastTime == time) {
            if ((time & SEQUENCE_MASK) == (sequence = ++sequence & SEQUENCE_MASK)) {
                time = waitUntilNextTime(lastTime);
                sequence = time & SEQUENCE_MASK;
            }
        } else {
            sequence = time & SEQUENCE_MASK;
        }
        lastTime = time;
        return ((time - TIME_OFFSET) << TIMESTAMP_LEFT_SHIFT_BITS) | (workerId << WORKER_ID_LEFT_SHIFT_BITS) | sequence;
    }

    /**
     * 生成带有业务属性前缀的id，格式：prefix_10321031203189
     * @param prefix 自定义业务前缀
     * @return String
     */
    public synchronized String generateId(String prefix){
        if(StringUtils.isBlank(prefix)){
            return String.valueOf(generateId());
        }
        sb = new StringBuilder(prefix).append(DEFAULT_SEPARATOR).append(generateId());
        return sb.toString();
    }

    private static long waitUntilNextTime(final long lastTime) {
        long time = System.currentTimeMillis();
        while (time <= lastTime) {
            time = System.currentTimeMillis();
        }
        return time;
    }

    /**
     * 根据本机ip取模获取workId
     * @return long
     */
    private static long getWorkId(){
        long workId = -1;
        try{
            String address = InetAddress.getLocalHost().getHostAddress();
            long ip = Long.parseLong(address.replaceAll("\\.", ""));
            workId = ip % (1 << WORKER_ID_BITS);
            System.out.println("当前机器ip: " + address + ", 转换后的workId: " + workId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return workId;
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.format(DateUtil.date(TIME_OFFSET), "yyyy-MM-dd HH:mm:ss"));
        System.out.println(255255255255L % 8192);
        System.out.println(getWorkId());
        IdGenerator idGenerator1 = IdGenerator.getInstance(10);
        String id1 = idGenerator1.generateId("NEWS");
        System.out.println(id1);
        IdGenerator idGenerator = IdGenerator.getInstance(2);
        long id = idGenerator.generateId();
        print(id);
    }

    private static void print(long num) {
        System.out.println("10进制:" + num);
        System.out.println("10进制长度:" + Long.valueOf(num).toString().length());
        String hexStr = Long.toHexString(num);
        System.out.println("16进制:" + hexStr);
        System.out.println("16进制长度:" + hexStr.length());
        String numStr = Long.toBinaryString(num);
        System.out.println("2进制不换行输出：" + numStr);
        System.out.println("2进制长度:" + numStr.length());
        System.out.println("2进制从低位到高位换行输出");
        for (int i = numStr.length() - 1; i >= 0; i--) {
            System.out.println((numStr.length() - i) + " " + numStr.charAt(i));
        }
    }
}