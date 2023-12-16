package org.hf.boot.springboot.enumerate.repository.jpa;

import lombok.Data;
import org.hf.boot.springboot.enumerate.NewsStatus;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * spring data jpa实体
 */
@Entity
@Data
@Table(name = "test_enum")
public class JpaNewsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 在实体中的具体字段上配置枚举转换器
     * 自定义枚举转换及查询处理 - 11
     */
    @Convert(converter = JpaNewsStatusConverter.class)
    private NewsStatus status;
}