package org.hf.boot.springboot.constants;

/**
 * <p> 自定义重试类型枚举 </p >
 *
 * @author HUFEI
 * @date 2023-03-27
 **/
public enum RetryTypeEnum {

    /**
     * 自定义 key 前缀
     */
    METHOD("METHOD", "方法调用"),

    URL("URL", "地址调用");

    private final String code;

    private final String desc;

    RetryTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}