package org.hf.boot.springboot.enumerate.repository.jpa;

import org.hf.boot.springboot.enumerate.NewsStatus;

/**
 * spring data jpa自定义指定枚举转换处理器
 * 自定义枚举转换及查询处理 - 10
 */
public class JpaNewsStatusConverter extends CommonEnumAttributeConverter<NewsStatus> {

    public JpaNewsStatusConverter() {
        super(NewsStatus.values());
    }
}