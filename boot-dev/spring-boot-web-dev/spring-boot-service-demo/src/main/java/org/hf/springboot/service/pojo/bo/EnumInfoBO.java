package org.hf.springboot.service.pojo.bo;

import lombok.Data;

/**
 * <p> 枚举类枚举项信息BO </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/14 14:09
 */
@Data
public class EnumInfoBO {

    /**
     * 枚举类group
     */
    private String group;

    /**
     * 排序编号
     */
    private Integer orderNumber;

    /**
     * 父编码code
     */
    private String parentCode;

    /**
     * 编码
     */
    private Object value;

    /**
     * 标签描述
     */
    private String label;

}
