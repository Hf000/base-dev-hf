package org.hf.application.mybatis.multiple.datasource.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * <p> 设置数据源路由key，数据源AOP方式：默认情况下，所有的查询都走从库，插入/修改/删除走主库 </p>
 * 自定义实现多数据源 - 7
 * @author hufei
 * @date 2022/8/13 9:43
*/
@Aspect
@Component
public class DataSourceAop {
    @Pointcut("!@annotation(org.hf.application.mybatis.multiple.datasource.config.Master) " +
            "&& (execution(* org.hf..*.service..*.select*(..)) " +
            "|| execution(* org.hf..*.service..*.get*(..))" +
            "|| execution(* org.hf..*.service..*.find*(..)))")
    public void readPointcut() {
    }

    @Pointcut("@annotation(org.hf.application.mybatis.multiple.datasource.config.Master) " +
            "|| execution(* org.hf..*.service..*.insert*(..)) " +
            "|| execution(* org.hf..*.service..*.add*(..)) " +
            "|| execution(* org.hf..*.service..*.update*(..)) " +
            "|| execution(* org.hf..*.service..*.edit*(..)) " +
            "|| execution(* org.hf..*.service..*.delete*(..)) " +
            "|| execution(* org.hf..*.service..*.remove*(..))" +
            "|| execution(* org.hf..*.service..*.save*(..))")
    public void writePointcut() {
    }

    @Before("readPointcut()")
    public void read() {
        DBContextHolder.slave();
    }

    @Before("writePointcut()")
    public void write() {
        DBContextHolder.master();
    }

    /**
     * 另一种写法：if...else...  判断哪些需要读从数据库，其余的走主数据库
     */
//    @Before("execution(* org.hf.*.service.impl.*.*(..))")
//    public void before(JoinPoint jp) {
//        String methodName = jp.getSignature().getName();
//
//        if (StringUtils.startsWithAny(methodName, "get", "select", "find")) {
//            DBContextHolder.slave();
//        }else {
//            DBContextHolder.master();
//        }
//    }
}
