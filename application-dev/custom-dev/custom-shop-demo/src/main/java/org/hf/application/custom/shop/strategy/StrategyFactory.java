package org.hf.application.custom.shop.strategy;

import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 20:03
*/
@Component
@ConfigurationProperties(prefix = "strategy")
public class StrategyFactory implements ApplicationContextAware {

    //①注入ApplicationContext
    //vipOne:VipOneInstance
    //vipTwo:VipTwoInstance
    //vipThree:VipThreeInstance
    //vipFour:VipFourInstance
    private ApplicationContext act;

    //1:vipOne
    //2:vipTwo
    //3:vipThree
    //4:vipFour
    private Map<Integer,String> strategyMap;

    /***
     * 通过等级获取用户对应的策略实例
     */
    public VipMoney get(Integer level){
        //获取等级对应的策略实例ID
        String id = strategyMap.get(level);
        //根据策略实例ID从容器中获取策略实例
        return act.getBean(id,VipMoney.class);
    }


    //注入配置中的映射信息
    public void setStrategyMap(Map<Integer, String> strategyMap) {
        this.strategyMap = strategyMap;
    }

    //注入容器
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        act = applicationContext;
    }
}
