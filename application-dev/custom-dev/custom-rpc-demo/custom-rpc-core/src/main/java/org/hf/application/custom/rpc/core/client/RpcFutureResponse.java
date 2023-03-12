package org.hf.application.custom.rpc.core.client;

import org.hf.application.custom.rpc.core.base.RpcRequest;
import org.hf.application.custom.rpc.core.base.RpcResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 用于异步的响应处理
 */
public class RpcFutureResponse implements Future<RpcResponse> {

    //存储requestId与RpcFutureResponse对象的映射关系
    private static final Map<String, RpcFutureResponse> MAP = new ConcurrentHashMap<>();

    //是否处理完成，默认为false
    private boolean done = false;

    //定义一个对象用于锁操作
    private final Object lock = new Object();

    //响应对象
    private RpcResponse response;

    /**
     * 在构造函数中将当前对象存储到MAP中
     *
     * @param rpcRequest
     */
    public RpcFutureResponse(RpcRequest rpcRequest) {
        MAP.put(rpcRequest.getRequestId(), this);
    }

    /**
     * 根据请求id查询RpcFutureResponse对象
     *
     * @param requestId
     * @return
     */
    public static RpcFutureResponse getRpcFutureResponse(String requestId) {
        return MAP.get(requestId);
    }


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        //TODO 暂不实现
        return false;
    }

    @Override
    public boolean isCancelled() {
        //TODO 暂不实现
        return false;
    }

    /**
     * 是否完成
     *
     * @return
     */
    @Override
    public boolean isDone() {
        return done;
    }

    /**
     * 获取数据，没有超时时间
     *
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Override
    public RpcResponse get() throws InterruptedException, ExecutionException {
        return this.get(-1, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取数据，指定了超时时间
     *
     * @param timeout 单位为毫秒
     * @param unit
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Override
    public RpcResponse get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException {
        if (isDone()) {
            return this.response;
        }

        synchronized (lock) { //确保只有一个线程在一个时刻执行操作
            try {
                if (timeout < 0) { //如果没有设置超时时间的话，就一直等待
                    lock.wait();
                } else {
                    // 如果设置了超时时间的话，就在给定时间内进行等待
                    long timeoutMillis = (TimeUnit.MILLISECONDS == unit) ? timeout : TimeUnit.MILLISECONDS.convert(timeout, unit);
                    lock.wait(timeoutMillis);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (isDone()) {
            return this.response;
        }

        throw new RuntimeException("获取数据，等待超时。。。");
    }

    public void setResponse(RpcResponse response){
        this.response = response;
        synchronized (lock) { //确保只有一个线程在一个时刻执行操作
            this.done = true;
            this.lock.notifyAll(); //唤醒等待的线程
        }

        //删除已缓存的对象
        MAP.remove(response.getRequestId());
    }
}