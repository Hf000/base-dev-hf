package org.hf.domain.model.frame.web.config.logback;

import ch.qos.logback.core.PropertyDefinerBase;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * 将本地IP拼接到日志文件名中，以区分不同实例，避免存储到同一位置时的覆盖冲突问题<br/>
 */
@Slf4j
public class LogbackIpDefine extends PropertyDefinerBase {
    
    private String getDockerIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("fail to get ip...", e);
        }
        return UUID.randomUUID().toString().replace("-", "").substring(0, 20);
    }
    
    @Override
    public String getPropertyValue() {
        return getDockerIp();
    }
}