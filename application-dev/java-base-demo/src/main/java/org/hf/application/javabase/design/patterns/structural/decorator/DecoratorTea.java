package org.hf.application.javabase.design.patterns.structural.decorator;

/**
 * <p> 装饰者抽象 </p>
 *
 * @author hufei
 * @date 2022/10/6 11:12
 */
public abstract class DecoratorTea implements Tea {

    /**
     * 被增强的对象
     */
    private Tea tea;

    /**
     * 注入被增强的对象
     * @param tea 入参
     */
    public void setTea(Tea tea) {
        this.tea = tea;
    }

    @Override
    public void making() {
        //调用被增强对象
        tea.making();
    }


}
