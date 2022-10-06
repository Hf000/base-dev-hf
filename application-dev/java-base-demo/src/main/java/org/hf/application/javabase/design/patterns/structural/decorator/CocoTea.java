package org.hf.application.javabase.design.patterns.structural.decorator;

/**
* <p> 装饰者模式 </p>
* @author hufei
* @date 2022/7/13 21:22
*/
public class CocoTea extends DecoratorTea {

    @Override
    public void making() {
        //调用被扩展的对象方法
        super.making();
        //增加扩展
        addCoco();
    }

    /**
     * 扩展方法
     */
    public void addCoco(){
        System.out.println("添加椰汁！");
    }
}
