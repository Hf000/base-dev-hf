package org.hf.springboot.service.pojo.bo;

import lombok.Data;

import java.util.List;

/**
 * <p> 枚举查询BO </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/14 14:14
 */
@Data
public class EnumQueryBO {

    /**
     * 枚举类分组group
     */
    private List<String> groups;

    /**
     * 语言: 中文,cn; 英文,en
     */
    private String language;

}
