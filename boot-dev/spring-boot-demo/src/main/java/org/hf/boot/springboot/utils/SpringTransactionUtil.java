package org.hf.boot.springboot.utils;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p> spring事务工具 </p >
 *  判断当前是否存在事务、添加钩子函数都是依赖线程变量的。因此，我们在使用过程中，一定要避免切换线程。否则会出现不生效的情况。
 **/
public class SpringTransactionUtil {

    @SuppressWarnings("all")
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @SuppressWarnings("all")
    public void sendLog() {
        // 判断当前是否存在事务
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            // 无事务，异步发送消息给kafka
            executor.submit(() -> {
                // 发送消息给kafka
                try {
                    // 发送消息给kafka
                } catch (Exception e) {
                    // 记录异常信息，发邮件或者进入待处理列表，让开发人员感知异常
                }
            });
            return;
        }
        // 有事务，则添加一个事务同步器，并重写afterCompletion方法（此方法在事务提交后会做回调）
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_COMMITTED) {
                    // 事务提交后，再异步发送消息给kafka
                    executor.submit(() -> {
                        try {
                            // 发送消息给kafka
                        } catch (Exception e) {
                            // 记录异常信息，发邮件或者进入待处理列表，让开发人员感知异常
                        }
                    });
                }
            }
        });
    }

}