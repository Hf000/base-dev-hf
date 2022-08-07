package org.hf.application.javabase.algorithm;

/**
 * <p> 雪花算法 </p>
 * 组成：1bit + 41位时间戳 + 10位机器ID + 12序列号
 * 1bit：符号位，0代表正数，1代表负数；
 * 41位时间戳，系统时间-系统上线时间，单位毫秒              左移机器码+序列号位数和
 * 10位机器的唯一标识码                                 左移序列号位数
 * 12位序列号，同一毫秒同一台机器递增
 *      将以上的向左位移后做“或”运算
 * 机器ID和序列号可以适当调整位数，然后加入适当位数的业务ID，整个合起来要有64位
 * 这里实现 1bit + 41位时间戳 + 5位业务ID + 7位机器码 + 10 序列号
 *
 *  位运算
 *      1.向左移几位就相当于乘以2的几次方；2.向右移动几位就是除以2的几次方。
 *
 *  逻辑运算
 *      1.“|”或运算：有1则为1，没1则为0；
 *      2.“&”与运算：同时为1则为1，否则为0；
 *      3.“^”异或运算：相同为0，不同为1.
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 16:46
 */
public class Snowflake {

    //系统上线时间
    private final long startTime = 1600012800000L;       //不能大于当前时间   目的：让每个部分从最小开始
    //业务ID
    private final long businessType;
    //机器id
    private final long workId;
    //序列号
    private long serialNum = 0L;
    //业务ID长度
    private final int businessTypeBits = 5;
    //机器id长度
    private final int workIdBits = 7;
    //序列号长度
    private final int serialNumBits = 10;
    //机器id向左位移长度
    private final int workIdShift = serialNumBits;
    //业务Id向左位移长度
    private final int businessTypeShift = workIdBits + workIdShift;
    //时间戳向左位移长度
    private final int timeStampShift = businessTypeShift + businessTypeBits;
    //上次执行的时间
    private long lastTimeStamp = 0L;
    //序列号的最大值  二进制10位最大为1023
    private final long serialNumMax = -1^(-1L << serialNumBits);    //二进制”^“异或运算,相同为0，不同为1， 位运算“<<” 向左位移相当于10进制数乘以2
    //创建有参构造，传入业务ID和机器ID
    public Snowflake(long businessType, long workId) {
        this.businessType = businessType;
        this.workId = workId;
    }

    //获取唯一id
    public synchronized long getId() {//防止并发操作
        long timeStamp = System.currentTimeMillis();//获取当前时间戳；
        if (timeStamp == lastTimeStamp) {       //如果是同一毫秒执行的，则serialNum+1
            /*1023二进制1111111111,1024的二进制10000000000， 所以与的结果为0； 1111111111与上比他小的数结果都是那个小的数*/
            serialNum = (serialNum + 1) & serialNumMax;//二进制“&”与运算 同位上都是1时才为1，否则为0
            if (serialNum == 0) {       //说明超过了最大值
                timeStamp = waitNextMills(timeStamp);//超过了最大时间就拿下一毫秒的时间戳
            }
        } else {
            /*不是同一毫秒的将都从0开始, 如果这里取值的时候都不是发生在同一秒，那么生成的id就都是偶数，原因：二进制转十进制如果二进制数末尾为0则转成的十进制
            为偶数（因为2的n次方除了0次方以外的结果都是偶数，只有最后一个2的0次方为奇数1，偶数+奇数=奇数，偶数+偶数=偶数），这样就导致生成的id都是偶数不利
            于后期分库分表等业务扩展*/
            //serialNum = 0;
            serialNum = timeStamp & 1;//取时间戳的最后一位，二进制与1要么是0要么是1，
        }
        lastTimeStamp = timeStamp;
        /*“|”或运算，0|0=0，0|1=1，1|0=1，1|1=1 */
        return ((timeStamp - startTime) << timeStampShift) | (businessType << businessTypeShift) | (workId << workIdShift) | serialNum;
    }

    //拿下一毫秒的时间戳
    private long waitNextMills(long timeStamp) {
        long nowTimeStamp = System.currentTimeMillis();
        while(nowTimeStamp <= timeStamp) {
            nowTimeStamp = System.currentTimeMillis();
        }
        return nowTimeStamp;
    }

}
