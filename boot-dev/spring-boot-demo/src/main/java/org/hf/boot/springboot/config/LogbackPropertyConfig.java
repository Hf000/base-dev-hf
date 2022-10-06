package org.hf.boot.springboot.config;

import ch.qos.logback.core.PropertyDefinerBase;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * <p> logback日志自定义参数值获取 </p >
 * 将IP拼接到日志文件名中，以区分不同实例，避免存储到同一位置时的覆盖冲突问题
 *
 * @author HUFEI
 * @date 2022-09-26
 **/
@Slf4j
public class LogbackPropertyConfig extends PropertyDefinerBase {

    private String getIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("fail to get ip", e);
        }
        return UUID.randomUUID().toString().replace("-", "").substring(0, 20);
    }

    @Override
    public String getPropertyValue() {
        return getIp();
    }
}