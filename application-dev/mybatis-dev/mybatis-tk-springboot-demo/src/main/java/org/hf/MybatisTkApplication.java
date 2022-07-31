package org.hf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <p> 启动类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/2/21 21:13
 * 开启通用mapper的包扫描  tk.mybatis.spring.annotation.MapperScan
 */
@MapperScan("org.hf.application.mybatis.tk.**.mapper")
@SpringBootApplication
public class MybatisTkApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisTkApplication.class, args);
    }

}
