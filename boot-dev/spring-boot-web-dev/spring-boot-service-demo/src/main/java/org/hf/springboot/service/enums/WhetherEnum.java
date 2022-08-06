package org.hf.springboot.service.enums;

import org.hf.common.config.enumerate.JavaEnum;
import org.hf.common.config.enumerate.NameIntegerValueEnum;
import org.hf.springboot.service.constants.ServiceEnumGroupConstant;

/**
 * <p> 是否枚举 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/14 14:53
 */
@JavaEnum(value = ServiceEnumGroupConstant.WHETHER_ENUM)
public enum WhetherEnum implements NameIntegerValueEnum {
    /**
     * 否
     */
    N(0, "否"),
    /**
     * 是
     */
    Y(1, "是");

    private final Integer value;
    private final String label;

    WhetherEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getName() {
        return label;
    }
}
