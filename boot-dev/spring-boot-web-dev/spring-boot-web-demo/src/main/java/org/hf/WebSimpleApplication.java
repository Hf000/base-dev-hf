package org.hf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p> 启动类 </p>
 * //@MapperScan //mybatis的mapper扫描
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/9 10:11
 */
@SpringBootApplication
@MapperScan("org.hf.springboot.**.dao")
public class WebSimpleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSimpleApplication.class, args);
    }

}
