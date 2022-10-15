package org.hf.springcloud.api.provider.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> Feign日志配置类 </p>
 * //@Configuration //标识当前类为配置类
 * @author hufei
 * @date 2022/8/21 17:12
*/
@Configuration
public class FeignConfig {

    /**
     * //@Bean  //将当前对象注入到spring容器中
     * @return Logger.Level
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        /*日志级别
         * NONE：不记录任何日志信息，这是默认值；
         * BASIC：仅记录请求的方法，URL以及响应状态码和执行时间
         * HEADERS：在BASIC的基础上，额外记录了请求和响应的头信息
         * FULL：记录所有请求和响应的明细，包括头信息、请求体、元数据
         */
        return  Logger.Level.FULL;
    }
}
