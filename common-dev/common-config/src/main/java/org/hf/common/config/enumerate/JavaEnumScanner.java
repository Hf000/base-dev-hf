package org.hf.common.config.enumerate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p> java枚举扫描器, 用于查找符合条件的java枚举类 </p>
 * 自定义枚举注解JavaEnum实现 - 4
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/14 9:42
 */
@Slf4j
public class JavaEnumScanner {

    private ClassPathScanningCandidateComponentProvider classPathScanner = new ClassPathScanningCandidateComponentProvider(false);
    
    public JavaEnumScanner() {
        inti();
    }

    private void inti() {
        classPathScanner.addIncludeFilter(new JavaEnumTypeFilter());
        classPathScanner.setResourcePattern("**/*Enum.class");
    }
    
    public Map<String, Class<? extends NameValueEnum<?>>> doScan(String...basePacksges){
        Map<String, Class<? extends NameValueEnum<?>>> cache = new HashMap<>();
        Assert.notEmpty(basePacksges, "at least one base package must be specified");
        ClassLoader classLoader = classPathScanner.getResourceLoader().getClassLoader();
        for (String basePacksge : basePacksges) {
            Set<BeanDefinition> candidates = classPathScanner.findCandidateComponents(basePacksge);
            for (BeanDefinition bd : candidates) {
                ScannedGenericBeanDefinition sgbd = (ScannedGenericBeanDefinition) bd;
                AnnotationMetadata metadata = sgbd.getMetadata();
                Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(JavaEnum.class.getName());
                String enumGroup = (String) annotationAttributes.get("group");
                if (null == enumGroup || enumGroup.trim().isEmpty()) {
                    enumGroup = (String) annotationAttributes.get("value");
                }
                if (null == enumGroup || enumGroup.trim().isEmpty()) {
                    enumGroup = metadata.getClassName();
                }
                Class<? extends NameValueEnum<?>> old = cache.get(enumGroup);
                if (old != null) {
                    throw new RuntimeException(String.format("枚举类元注解@JavaEnum的配置值[%s]重复了,请检查类为[%s]与[%s]", enumGroup, old.getName(), sgbd.getBeanClassName()));
                }
                Class<?> enumClass = null;
                try {
                    enumClass = sgbd.resolveBeanClass(classLoader);
                } catch (ClassNotFoundException e) {
                    log.error("查询类异常", e);
                }
                if (null == enumClass) {
                    continue;
                }
                if (!NameValueEnum.class.isAssignableFrom(enumClass)) {
                    continue;
                }
                cache.put(enumGroup, (Class<? extends NameValueEnum<?>>) enumClass);
            }
        }
        return cache;
    }

}
