package org.hf.application.javabase.jdk8.optional;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

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
public class Student {

    private int age;

    private String name;

}
