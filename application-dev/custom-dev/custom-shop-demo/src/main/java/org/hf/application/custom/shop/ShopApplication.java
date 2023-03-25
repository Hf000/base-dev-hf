package org.hf.application.custom.shop;

import org.springframework.aop.framework.AopContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * <p> 服务启动类 </p>
 * //@EnableAspectJAutoProxy 开启代理支持 AopContext.currentProxy()
 *
 * @author hufei
 * @date 2022/8/7 16:57
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class ShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }

}
