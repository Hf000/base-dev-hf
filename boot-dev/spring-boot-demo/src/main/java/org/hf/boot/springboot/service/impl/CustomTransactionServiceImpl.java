package org.hf.boot.springboot.service.impl;

import org.hf.boot.springboot.error.BusinessException;
import org.hf.boot.springboot.pojo.dto.UserInfoReq;
import org.hf.boot.springboot.service.CustomTransactionService;
import org.hf.boot.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;

/**
 * <p> 事务处理示例 </p >
 * @author hf
 **/
@Service
public class CustomTransactionServiceImpl implements CustomTransactionService {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private UserService userService;

    @Autowired
    private DataSource dataSource;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transactionSubmitAfterLogic() {
        // 操作数据库业务逻辑
        // 事务提交后的前后置处理参考: https://www.cnblogs.com/ciel717/p/16190723.html
        // 处理事务提交后立刻执行的业务逻辑方式一
        transactionAfterCommit();
        // 处理事务提交后立刻执行的业务逻辑方式二
        // 这里发布事件后,需要定义一个事务监听器:
        // org.hf.boot.springboot.service.impl.CustomTransactionServiceImpl.MyTransactionListener
        applicationEventPublisher.publishEvent(new MyAfterTransactionEvent(this, new Object()));
    }

    /**
     * 事务提交后置处理,也可以通过spring的事务监听方式实现@TransactionalEventListener
     */
    public void transactionAfterCommit() {
        // 判断当前是否在spring事务中, [另外一种方式:org.hf.boot.springboot.utils.SpringTransactionUtil]
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new BusinessException("必须在事务中执行此方法");
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                // 在事务提交后执行此处逻辑, 即使此处抛出异常也不会导致事务回滚
            }
        });
    }

    /**
     * 事务监听器
     */
    @Component
    private static class MyTransactionListener {

        /**
         * 事件提交后的处理
         * @param event 事件对象
         * 监听阶段的四种配置如下:
         *  1.BEFORE_COMMIT: 指定目标方法在事务commit之前执行
         *  2.AFTER_COMMIT: 指定目标方法在事务commit之后执行
         *  3.AFTER_ROLLBACK: 指定目标方法在事务rollback之后执行
         *  4.AFTER_COMPLETION: 指定目标方法在事务完成时执行，这里的完成是指无论事务是成功提交还是事务回滚了
         */
        @TransactionalEventListener(
                // 监听的阶段
                phase = TransactionPhase.AFTER_COMMIT,
                // 若没有事务的时候，对应的event是否已经执行  默认值为false表示没事务就不执行了
                fallbackExecution = false,
                // 监听的事件类型, 默认可以处理所有类型的事件
                classes = MyAfterTransactionEvent.class)
        public void onFinishTransactionEvent(MyAfterTransactionEvent event) {
            Object source = event.getSource();
            // 事务提交之后执行的业务逻辑
        }
    }

    /**
     * 自定义一个事件，继承自ApplicationEvent
     */
    private static class MyAfterTransactionEvent extends ApplicationEvent {

        private static final long serialVersionUID = 7044172134078820457L;

        /**
         * 事件的构造方法
         * @param source 发布源，也就是发布事件的对象
         * @param paramObject 参数
         */
        public MyAfterTransactionEvent(Object source, Object paramObject) {
            super(source);
            // 自定义业务逻辑处理
        }
    }

    @SuppressWarnings("all")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void asyncTransactionLoseEfficacy() throws Exception {
        // 通过事务管理器对象获取当前线程的数据库连接对象的持有对象, 此对象存储在当前线程的TreadLocal中
        ConnectionHolder connectionHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
        UserInfoReq userInfo = new UserInfoReq();
        userInfo.setUserName("主线程");
        userInfo.setPassword("123d");
        userService.addUserInfo(userInfo);
        Thread thread = new Thread(() -> {
            // 绑定主线程的connection到子线程中
            TransactionSynchronizationManager.bindResource(dataSource, connectionHolder);
            UserInfoReq userInfo2 = new UserInfoReq();
            userInfo2.setUserName("子线程");
            userInfo2.setPassword("123d");
            userService.addUserInfo(userInfo2);
        });
        // 开启新的子线程
        thread.start();
        // 等待线程执行结束
        thread.join();
    }
}