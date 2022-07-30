package org.hf.common.web.utils;

/**
 * <p> 请求参数处理 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/9 16:26
 */
public class RequestParamUtil {

    private static final ThreadLocal<String> LOCAL_REQUEST_ID = new ThreadLocal<>();

    /**
     * 设置本地线程请求id
     * @param requestId 请求id
     */
    public static void resetRequestId(String requestId) {
        LOCAL_REQUEST_ID.remove();
        LOCAL_REQUEST_ID.set(requestId);
    }

    /**
     * 获取本地线程请求id
     * @return 返回请求id
     */
    public static String get() {
        return LOCAL_REQUEST_ID.get();
    }

}
