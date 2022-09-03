package org.hf.application.javabase.basic.extend;

/**
 * <p> 子类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/13 20:12
 */
public class Son extends Parent {

    private String variable = "variable-son";

    private String variableThree = "variableThree-son";

    @Override
    public void methodTwo() {
        // 子类调用父类的方法, 父类方法调用的成员变量都是父类的
        super.methodTwo();
        // 子类重写父类方法, 不能访问父类私有成员变量
        System.out.println("子类成员变量的值:" + variable);
        // 子类重写父类方法, 能访问父类非私有成员变量
        System.out.println("子类成员变量访问父类非私有成员变量的值:" + variableTwo);
        // 子类重写父类方法, 子类会覆盖父类同名非私有成员变量
        System.out.println("子类覆盖父类成员变量的值:" + variableThree);
    }
}
