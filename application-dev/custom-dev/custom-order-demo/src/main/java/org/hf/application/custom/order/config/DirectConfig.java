package org.hf.application.custom.order.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectConfig {

    @Bean
    public Queue queue() {
        return new Queue("promotion.order");
    }
}