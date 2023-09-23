package org.hf.common.web.currentlimiting.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

/**
 * 基于 sentinel 实现接口限流
 * 这里采用QPS(每秒查询率)来进行接口请求数限制
 * @author hf
 */
@Aspect
@Slf4j
@Component
public class SentinelLimiterAop {

    @Around("@annotation(org.hf.common.web.currentlimiting.sentinel.SentinelLimiter)")
    public Object around(ProceedingJoinPoint joinPoint) {
        //1、获取当前的调用方法
        Method currentMethod = getCurrentMethod(joinPoint);
        if (Objects.isNull(currentMethod)) {
            return null;
        }
        //2、从方法注解定义上获取限流的类型
        SentinelLimiter annotation = currentMethod.getAnnotation(SentinelLimiter.class);
        String resourceName = annotation.resourceName();
        if(StringUtils.isEmpty(resourceName)){
            throw new RuntimeException("资源名称为空");
        }
        int limitCount = annotation.limitCount();
        //3.初始化限流规则
        initFlowRule(resourceName,limitCount);
        Entry entry = null;
        Object result = null;
        try {
            // 标记受保护的资源,如果抛出异常则表示限流了
            entry = SphU.entry(resourceName);
            try {
                result = joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } catch (BlockException ex) {
            // 资源访问阻止，被限流或被降级   在此处进行相应的处理操作
            log.error("blocked");
        } catch (Exception e) {
            // 失败异常记录,跟踪本次调用资源的进入情况,用于异常熔断降级
            Tracer.traceEntry(e, entry);
        } finally {
            if (entry != null) {
                // 退出,告知sentinel可以进行资源清理和数据统计
                entry.exit();
            }
        }
        return result;
    }

    /**
     * 初始化sentinel的限流配置
     * @param resourceName  资源名称
     * @param limitCount    触发限流的次数
     */
    private static void initFlowRule(String resourceName, int limitCount) {
        ArrayList<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        // 设置受保护的资源
        rule.setResource(resourceName);
        // 设置流量规则 QPS
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // 设置保护阈值
        rule.setCount(limitCount);
        rules.add(rule);
        // 加载配置好的规则
        FlowRuleManager.loadRules(rules);
    }

    private Method getCurrentMethod(JoinPoint joinPoint) {
        Method[] methods = joinPoint.getTarget().getClass().getMethods();
        Method target = null;
        for (Method method : methods) {
            if (method.getName().equals(joinPoint.getSignature().getName())) {
                target = method;
                break;
            }
        }
        return target;
    }
}