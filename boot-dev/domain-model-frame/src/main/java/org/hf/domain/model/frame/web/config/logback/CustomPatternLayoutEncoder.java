package org.hf.domain.model.frame.web.config.logback;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义日志输出编码格式
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomPatternLayoutEncoder extends PatternLayoutEncoder {

    @Override
    public void start() {
        // 这里进行自定义处理, 例如日志脱敏, 这里走默认逻辑
        super.start();
    }
}