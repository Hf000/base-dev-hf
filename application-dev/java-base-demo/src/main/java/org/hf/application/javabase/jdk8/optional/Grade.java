package org.hf.application.javabase.jdk8.optional;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

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
public class Grade {

    private List<Student> students;

}
