package org.hf.boot.springboot.export;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 流式导出excel实现 - 3
 * 导出结果集处理工厂类
 */
public interface ResultHandlerFactory {

    /**
     * 获取导出结果处理集
     * @param response  请求响应对象
     * @return 导出结果
     * @throws IOException 异常
     */
    CommonResultHandler<?, ?> getResultHandler(HttpServletResponse response) throws IOException;

}