package org.hf.boot.springboot.service;

/**
 * <p> 事务处理 </p >
 * @author hf
 **/
public interface CustomTransactionService {

    /**
     * 事务提交后逻辑处理
     */
    void transactionSubmitAfterLogic();

    /**
     * 异步线程失效解决方式
     * 或者采用事务手动提交方式, 实现:org.hf.boot.springboot.aop.CustomTransactionalAspect
     */
    void asyncTransactionLoseEfficacy() throws Exception;

}