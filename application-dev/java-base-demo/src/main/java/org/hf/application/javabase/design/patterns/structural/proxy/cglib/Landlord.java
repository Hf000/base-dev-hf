package org.hf.application.javabase.design.patterns.structural.proxy.cglib;

/**
 * <p> 需要被代理的对象 </p>
 * @author hufei
 * @date 2022/10/6 10:48
*/
public class Landlord {

    /**
     * 收房组方法
     * @param name 入参
     */
    public void pay(String name){
        System.out.println(name);
    }
}
