package org.hf.application.dubbo.provider.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p> 启动类 </p >
 *
 * @author hufei
 * @date 2022-09-27
 **/
@SpringBootApplication
@MapperScan("org.hf.application.dubbo.**.mapper")
public class DubboProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboProviderApplication.class, args);
    }

}