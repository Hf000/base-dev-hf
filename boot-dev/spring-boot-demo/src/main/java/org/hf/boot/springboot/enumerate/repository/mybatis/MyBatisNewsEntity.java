package org.hf.boot.springboot.enumerate.repository.mybatis;

import lombok.Data;
import org.hf.boot.springboot.enumerate.NewsStatus;

/**
 * mybatis实体
 */
@Data
public class MyBatisNewsEntity {

    private Integer id;

    private NewsStatus status;
}