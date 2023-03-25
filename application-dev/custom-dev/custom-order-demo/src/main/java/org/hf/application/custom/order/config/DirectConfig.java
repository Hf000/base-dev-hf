package org.hf.application.custom.order.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> RabbitMQ的队列配置 </p>
 * @author hufei
 * @date 2023/3/25 17:26
*/
@Configuration
public class DirectConfig {

    @Bean
    public Queue queue() {
        return new Queue("promotion.order");
    }
}