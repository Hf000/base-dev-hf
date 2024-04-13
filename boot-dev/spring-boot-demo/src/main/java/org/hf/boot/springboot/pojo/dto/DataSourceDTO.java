package org.hf.boot.springboot.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 数据源信息业务类
 */
@Data
@Accessors(chain = true)
public class DataSourceDTO {

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
    private String driverClassName;
    /**
     * 数据库key，即保存Map中的key
     */
    private String key;
}