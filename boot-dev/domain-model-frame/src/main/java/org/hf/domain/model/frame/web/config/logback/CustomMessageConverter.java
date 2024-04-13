package org.hf.domain.model.frame.web.config.logback;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.alibaba.fastjson2.JSON;
import org.slf4j.helpers.MessageFormatter;

import java.util.stream.Stream;

/**
 * 日志输出转换器
 */
public class CustomMessageConverter extends MessageConverter {

    @Override
    public String convert(ILoggingEvent event) {
        try {
            // 将对象进行json格式化打印出来
            Object[] args = event.getArgumentArray();
            if (null == args || args.length == 0) {
                return MessageFormatter.arrayFormat(event.getMessage(), null).getMessage();
            }
            Object[] objects = Stream.of(args).map(t -> {
                // 这里可以对敏感信息进行脱敏处理
                if (t instanceof String) {
                    // String类型直接打印
                    return t.toString();
                }
                return JSON.toJSONString(t);
            }).toArray();
            return MessageFormatter.arrayFormat(event.getMessage(), objects).getMessage();
        } catch (Exception e){
            return super.convert(event);
        }
    }
}