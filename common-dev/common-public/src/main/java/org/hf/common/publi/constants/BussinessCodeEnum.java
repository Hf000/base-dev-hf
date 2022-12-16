package org.hf.common.publi.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * <p> 业务编码枚举，数字&字母皆可，长度建议两位即可 </p >
 * 暂用于生成唯一编号
 *
 * @author HF
 * @date 2022-11-29
 **/
@Getter
@AllArgsConstructor
public enum BussinessCodeEnum {

    /**
     * 业务编码枚举
     */
    BUSSINESS_CODE("BC", "业务编码"),
    ;

    private final String code;
    private final String desc;

    public static BussinessCodeEnum matchByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (BussinessCodeEnum bussinessActionEnum : BussinessCodeEnum.values()) {
            if (bussinessActionEnum.getCode().equals(code)) {
                return bussinessActionEnum;
            }
        }
        return null;
    }

    public static String getDescByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return StringUtils.EMPTY;
        }
        for (BussinessCodeEnum s : values()) {
            if (s.code.equals(code)) {
                return s.desc;
            }
        }
        return StringUtils.EMPTY;
    }
}
