package org.hf.application.javabase.design.patterns.structural.proxy.aop;

/**
 * <p> 接口实现 </p>
 * @author hufei
 * @date 2022/10/6 10:33
*/
public class UserCardServiceImpl implements UserCardService {

    @Override
    public void card(String name){
        System.out.println(name+"打卡成功！");
    }
}
