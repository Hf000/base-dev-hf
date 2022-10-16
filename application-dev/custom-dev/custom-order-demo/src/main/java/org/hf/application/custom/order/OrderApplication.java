package org.hf.application.custom.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@MapperScan("org.hf.application.custom.order.mapper")
public class OrderApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(OrderApplication.class).run(args);
    }
}
