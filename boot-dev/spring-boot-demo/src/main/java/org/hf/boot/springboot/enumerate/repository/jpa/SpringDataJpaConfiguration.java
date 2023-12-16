package org.hf.boot.springboot.enumerate.repository.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * spring data jpa包扫描配置
 */
@Configuration
@EnableJpaRepositories(basePackages = {"org.hf.boot.springboot.enumerate.repository.jpa"})
public class SpringDataJpaConfiguration {
}