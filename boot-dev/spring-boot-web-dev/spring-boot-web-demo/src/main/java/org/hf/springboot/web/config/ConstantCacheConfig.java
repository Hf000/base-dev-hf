package org.hf.springboot.web.config;

import lombok.Data;
import org.hf.springboot.web.constants.ConstantNumber;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Constants;

import javax.annotation.PostConstruct;

/**
 * <p> 常量缓存, 将一类常量放到一起 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/14 15:14
 */
@Data
@Configuration
public class ConstantCacheConfig {

    private Constants constantNumber;

    /**
     * //@PostConstruct注解在对象注入完成时执行, 执行顺序: Constructor -> @Autowired -> @PostConstruct, 只执行一次, 此方法不能抛出异常
     */
    @PostConstruct
    public void init() {
        if (null == constantNumber) {
            constantNumber = new Constants(ConstantNumber.class);
        }
    }

}
