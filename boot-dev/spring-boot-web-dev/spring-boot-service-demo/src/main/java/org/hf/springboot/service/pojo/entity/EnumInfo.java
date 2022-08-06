package org.hf.springboot.service.pojo.entity;

import lombok.Data;

/**
 * <p> 枚举数据实体 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/14 14:18
 */
@Data
public class EnumInfo {

    /**
     * 枚举分组
     */
    private String group;

    /**
     * 父编码
     */
    private String parentCode;

    /**
     * 排序
     */
    private Integer orderNumber;

    /**
     * 枚举项编码
     */
    private Object value;

    /**
     * 标签描述
     */
    private String label;

}
