package org.hf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <p> springboot启动类 </p>
 * @author hufei
 * @date 2022/8/3 22:00
*/
@SpringBootApplication
@MapperScan("org.hf.**.dao")
public class MybatisMultipleDataSourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisMultipleDataSourceApplication.class, args);
    }
}