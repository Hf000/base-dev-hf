package org.hf.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <p> springboot启动类 </p>
 * @author hufei
 * @date 2022/8/3 22:00
*/
/**
 * springboot启动类注解
 */
@SpringBootApplication
/**
 * 开启扫描mybatis所有业务mapper接口的包路径, 整合通用mapper后需要将这个注解注释掉
 */
//@MapperScan("org.hf.**.dao")
/**
 * 整合通用mapper需要注释掉mybatis官方的@MapperScan注解，开启通用mapper的@MapperScan注解
 */
@MapperScan("org.hf.**.dao")
public class SpringBootDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }
}