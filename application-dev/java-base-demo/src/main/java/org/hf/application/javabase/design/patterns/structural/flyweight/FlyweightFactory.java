package org.hf.application.javabase.design.patterns.structural.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> 享元对象工厂 </p>
 * @author hufei
 * @date 2022/10/6 12:26
*/
public class FlyweightFactory {

    /**
     * 定义一个容器 存放变量
     */
    private static final Map<String,Flyweight> POOL = new HashMap<String,Flyweight>();

    /****
     * 获取Flyweight
     * @param extrinsic 入参
     * @return Flyweight
     */
    public static Flyweight getFlyweight(String extrinsic){
        Flyweight flyweight = null;
        //从池中获取该对象
        if(POOL.containsKey(extrinsic)){
            flyweight = POOL.get(extrinsic);
            System.out.println("已经"+extrinsic+"，直接从Pool中取");
        }else{
            //创建Flyweight
            flyweight = new ConcreteFlyweight(extrinsic);
            //将创建的Flyweight存入到Pool中
            POOL.put(extrinsic,flyweight);
            System.out.println("创建"+extrinsic+",并从池中取出！");
        }
        return flyweight;
    }
}
