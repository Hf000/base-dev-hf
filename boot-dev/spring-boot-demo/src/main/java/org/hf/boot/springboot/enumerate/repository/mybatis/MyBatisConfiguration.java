package org.hf.boot.springboot.enumerate.repository.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis包扫描配置
 */
@Configuration
@MapperScan(basePackages = {"org.hf.boot.springboot.enumerate.repository.mybatis"})
public class MyBatisConfiguration {
}