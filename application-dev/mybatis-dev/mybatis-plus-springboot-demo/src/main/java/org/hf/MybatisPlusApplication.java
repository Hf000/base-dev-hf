package org.hf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>  </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/11/2 16:11
 * 扫描mapper包, 如果Mapper类加了@Mapper注解就不需要这个扫描注解了
 */
@MapperScan("org.hf.**.mapper")
@SpringBootApplication
public class MybatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(org.hf.MybatisPlusApplication.class, args);
    }

}
