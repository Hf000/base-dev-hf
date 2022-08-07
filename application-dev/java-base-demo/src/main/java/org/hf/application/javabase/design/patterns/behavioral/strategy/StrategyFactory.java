package org.hf.application.javabase.design.patterns.behavioral.strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/8/7 15:47
*/
public class StrategyFactory {

    /**
     * 定义一个Map对象存储所有策略
     */
    private static final Map<Integer, Strategy> STRATEGY_MAP = new HashMap<>();

    //初始化所有策略并存入到Map中
    static {
        STRATEGY_MAP.put(1,new Vip1());
        STRATEGY_MAP.put(2,new Vip2());
        STRATEGY_MAP.put(3,new Vip3());
    }

    /**
     * 对外提供一个方法用于根据用户提供的等级获取指定策略
     */
    public static Strategy get(Integer level){
        return STRATEGY_MAP.get(level);
    }
}
