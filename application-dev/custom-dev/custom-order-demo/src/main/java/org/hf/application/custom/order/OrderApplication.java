package org.hf.application.custom.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * <p> 服务启动类 </p>
 * @author hufei
 * @date 2023/3/25 17:14
 * //@MapperScan mybatis的mapper扫描注解
*/
@SpringBootApplication
@MapperScan("org.hf.application.custom.order.mapper")
public class OrderApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(OrderApplication.class).run(args);
    }
}
