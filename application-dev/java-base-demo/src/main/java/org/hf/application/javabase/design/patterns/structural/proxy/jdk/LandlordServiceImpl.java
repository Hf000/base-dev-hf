package org.hf.application.javabase.design.patterns.structural.proxy.jdk;

/**
 * <p> 接口实现 </p>
 * @author hufei
 * @date 2022/10/6 10:54
*/
public class LandlordServiceImpl implements LandlordService {

    @Override
    public void pay(String name){
        System.out.println(name);
    }
}
