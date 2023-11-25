package org.hf.boot.springboot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hf.boot.springboot.utils.EnumUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 枚举值获取工厂
 * 自定义实现枚举值获取 - 4
 */
@Slf4j
@Component
public class EnumFactory {

    private Map<String, Class<?>> enumMap = new HashMap<>();

    private static final String SCAN_CLASSPATH_PATTERN = "classpath*:org/hf/boot/springboot/constants/**/*.class";

    /**
     * 初始化加载指定路径下的枚举类
     */
    @PostConstruct
    public void init() {
        log.info("EnumFactory.init() start");
        enumMap = EnumUtil.getEnumMap(SCAN_CLASSPATH_PATTERN);
        log.info("EnumFactory.init() end");
    }

    /**
     * 根据别名获取指定枚举类
     *
     * @param alias 枚举别名
     * @return 枚举类
     */
    public Class<?> getClass(String alias) {
        return enumMap.get(alias);
    }

    /**
     * 根据枚举别名获取指定枚举类的所有枚举项
     *
     * @param alias 别名
     * @return 枚举项集合
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List getList(String alias) {
        Class clazz = getClass(alias);
        if (clazz != null) {
            return EnumUtil.getEnumList(clazz);
        } else {
            return new ArrayList<>();
        }
    }
}