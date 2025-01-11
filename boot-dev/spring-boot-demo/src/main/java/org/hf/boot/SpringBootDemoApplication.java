package org.hf.boot;

import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.retry.annotation.EnableRetry;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <p> springboot启动类 </p>
 * //@SpringBootApplication //springboot启动类注解
 * //@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})  // 排除指定的类
 * //@MapperScan("org.hf.**.dao") //开启扫描mybatis所有业务mapper接口的包路径, 整合通用mapper后需要将这个注解注释掉
 * //@MapperScan("org.hf.**.dao") //整合通用mapper需要注释掉mybatis官方的@MapperScan注解，开启通用mapper的@MapperScan注解
 * // @EnableRetry spring开启重试
 * // @ComponentScan备注一下, 这里排除掉error.handler包加载的原因是因为响应结果进行统一封装,导致了swagger返回类型被包装后无法展示了, 已经处理了问题,这里注释掉
 * @author hufei
 * @date 2022/8/3 22:00
*/
@SpringBootApplication(exclude = {RedissonAutoConfiguration.class})
@MapperScan("org.hf.boot.**.dao")
@EnableRetry
//@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "org.hf.boot.springboot.error.handler.*")})
public class SpringBootDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }
}