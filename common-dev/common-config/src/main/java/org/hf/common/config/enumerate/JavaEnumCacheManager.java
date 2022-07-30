package org.hf.common.config.enumerate;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p> java枚举缓存管理类  用于缓存所有使用元注解@JavaEnum的枚举类 </p>
 * 自定义枚举注解JavaEnum实现 - 2
 * {@link JavaEnum}
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/12 21:08
 */
@Component
public class JavaEnumCacheManager implements InitializingBean {

    private static final AtomicReference<JavaEnumCacheManager> INSTANCE = new AtomicReference<>();

    private final Map<String, Class<? extends  NameValueEnum<?>>> CACHE = new HashMap<>();

    @Value("${javaEnum.scannerPackages:org.hf.**.enums}")
    private String basePackages;

    public JavaEnumCacheManager() {
        INSTANCE.compareAndSet(null, this);
    }

    public static JavaEnumCacheManager get() {
        return INSTANCE.get();
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, Class<? extends NameValueEnum<?>>> map = new JavaEnumScanner().doScan(basePackages);
        CACHE.putAll(map);
    }

    /**
     * 返回所有的枚举类分组码
     * @return 集合
     */
    public List<String> getGroups() {
        return new ArrayList<>(CACHE.keySet());
    }

    /**
     * 返回缓存中所有的java枚举类的枚举项
     * @return 集合
     */
    public Map<String, List<NameValueEnum<?>>> findAll() {
        Map<String, List<NameValueEnum<?>>> m = new HashMap<>();
        Set<String> keySet = CACHE.keySet();
        for (String group : keySet) {
            List<NameValueEnum<?>> list = findByGroup(group);
            m.put(group, list);
        }
        return m;
    }

    /**
     * 根据所有的枚举类分组码获取所有的java枚举类的枚举项
     * @param groups 分组码
     * @return 集合
     */
    public Map<String, List<NameValueEnum<?>>> findByGroups(List<String> groups) {
        Map<String, List<NameValueEnum<?>>> m = new HashMap<>();
        for (String g : groups) {
            List<NameValueEnum<?>> list = findByGroup(g);
            m.put(g, list);
        }
        return m;
    }

    /**
     * 根据枚举类的分组码查找对应的java枚举类集合
     * @param group 分组码
     * @return 集合
     */
    public List<NameValueEnum<?>> findByGroup(String group) {
        Class<?> enumClass = CACHE.get(group);
        if (null == enumClass) {
            return Collections.emptyList();
        }
        Enum<?>[] enums = NameValueEnum.getEnums((Class<Enum>) enumClass);
        return Lists.newArrayList((NameValueEnum<?>[]) enums);
    }

    /**
     * 根据枚举类的分组码查询对应的java枚举类
     * @param group 分组码
     * @return 类
     */
    public Class<? extends NameValueEnum<?>> findEnumByGroup(String group) {
        return CACHE.get(group);
    }

}
