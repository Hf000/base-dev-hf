package org.hf.common.config.elasticjob;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * <p> elastic-job参数配置类 </p>
 * 自定义封装elastic-job实现 - 2
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/9 21:32
 */
@Component
@PropertySource("classpath:application.properties")
public class ElasticJobProperties {

    /**
     * zookeeper服务链接地址
     */
    @Value("${ej.address:127.0.0.1:2181}")
    private String address;

    /**
     * zookeeper的命名空间
     */
    @Value("${ej.namespace:jobNameSpace}")
    private String namespace;

    /**
     * 等待重试的间隔时间的初始值,单位：毫秒
     */
    @Value("${ej.baseSleepTimeMilliseconds:1000}")
    private int baseSleepTimeMilliseconds;

    /**
     * 等待重试的间隔时间的最大值,单位：毫秒
     */
    @Value("${ej.maxSleepTimeMilliseconds:3000}")
    private int maxSleepTimeMilliseconds;

    /**
     * 最大重试次数
     */
    @Value("${ej.maxRetries:3}")
    private int maxRetries;

    /**
     * 是否改变初始值
     */
    @Value("${ej.flag:false}")
    private boolean flag;

    /**
     * 是否开启定时任务
     */
    @Value("${ej.open:true}")
    private boolean open;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public int getBaseSleepTimeMilliseconds() {
        return baseSleepTimeMilliseconds;
    }

    public void setBaseSleepTimeMilliseconds(int baseSleepTimeMilliseconds) {
        this.baseSleepTimeMilliseconds = baseSleepTimeMilliseconds;
    }

    public int getMaxSleepTimeMilliseconds() {
        return maxSleepTimeMilliseconds;
    }

    public void setMaxSleepTimeMilliseconds(int maxSleepTimeMilliseconds) {
        this.maxSleepTimeMilliseconds = maxSleepTimeMilliseconds;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public String toString() {
        return "ElasticJobProperties{" +
                "address='" + address + '\'' +
                ", namespace='" + namespace + '\'' +
                ", baseSleepTimeMilliseconds=" + baseSleepTimeMilliseconds +
                ", maxSleepTimeMilliseconds=" + maxSleepTimeMilliseconds +
                ", maxRetries=" + maxRetries +
                ", flag=" + flag +
                ", open=" + open +
                '}';
    }
}
