package org.hf.application.javabase.jdk8.optional;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Optional;

/**
 * <p>  </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/13 20:55
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class DemoDTO {

    private String area;

    /**
     * 可以判断参数是null还是不传
     */
    private Optional<String> city;

    private Optional<String> province;

}
