package org.hf.boot.springboot.pojo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 数据源信息实体类
 */
@Getter
@Setter
@ToString
@Table(name = "data_source_info")
public class DataSourceInfo {

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer id;

    /**
     * 数据库地址
     */
    private String url;

    /**
     * 数据库用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 数据库驱动
     */
    @Column(name = "driver_class_name")
    private String driverClassName;

    /**
     * 数据库name，即保存Map中的key
     */
    private String name;
}