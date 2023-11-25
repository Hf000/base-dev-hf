package org.hf.boot.springboot.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hf.boot.springboot.annotations.CustomEnum;
import org.hf.boot.springboot.service.BaseEnum;

/**
 * <p> 枚举测试 </p >
 *
 * @author hf
 * @date 2023-11-20
 **/
@Getter
@AllArgsConstructor
@CustomEnum("CustomJavaEnum")
public enum CustomJavaEnum implements BaseEnum<String, String> {

    CUSTOM("CUSTOM", "测试枚举"),
    DEFINE("DEFINE", "自定义枚举"),
    ;

    private final String code;

    private final String value;
}