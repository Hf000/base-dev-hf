package org.hf.application.javabase.basic.extend;

/**
 * <p> 父类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/13 20:10
 */
public class Parent {

    private String variable = "variable";

    public String variableTwo = "variableTwo";

    public String variableThree = "variableThree";

    public void method() {
        System.out.println("成员变量的值:" + variable);
    }

    public void methodTwo() {
        System.out.println("成员变量的值:" + variableTwo);
    }

}
