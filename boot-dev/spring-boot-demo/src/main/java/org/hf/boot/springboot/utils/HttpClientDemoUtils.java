package org.hf.boot.springboot.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * <p> httpClient请求demo </p >
 * 参考: <a href="https://www.cnblogs.com/myitnews/p/12195340.html">...</a>
 **/
@Slf4j
public class HttpClientDemoUtils {

    /**
     * Basic认证
     */
    private static final CredentialsProvider CREDENTIALS_PROVIDER = new BasicCredentialsProvider();

    /**
     * httpClient
     */
    private static final CloseableHttpClient HTTP_CLIENT;

    /**
     * 异步http请求客户端
     */
    private static final CloseableHttpAsyncClient HTTP_ASYNC_CLIENT;

    /**
     * httpGet方法
     */
    private static final HttpGet HTTP_GET;

    /**
     * 请求配置
     */
    private static final RequestConfig REQUEST_CONFIG;

    /**
     * 响应处理器
     */
    private static final ResponseHandler<String> RESPONSE_HANDLER;

    /**
     * jackson解析工具
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private HttpClientDemoUtils(){};

    static {
        // 1.配置保活连接参数, 注意:如果配置了nginx的话, nginx也要设置面向两端的keep-alive
        //设置系统参数
        // 设置保持活力的最大空闲连接数,http.keepAlive必须为true
        System.setProperty("http.maxConnections", "10");
        // 保持连接
        System.setProperty("http.keepAlive", "true");
        //自定义http的连接存活策略, 通过Keep-Alive头信息获得timeout值作为超时时间, 这个是默认实现故此自定义时可按业务场景修改
        ConnectionKeepAliveStrategy myStrategy = (response, context) -> {
            HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext()) {
                HeaderElement he = it.nextElement();
                String param = he.getName();
                String value = he.getValue();
                if (value != null && "timeout".equalsIgnoreCase(param)) {
                    return Long.parseLong(value) * 1000;
                }
            }
            //如果没有约定，则默认定义时长为60s
            return 60 * 1000;
        };
        //配置http连接池管理器(默认配置该管理器), 线程安全
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        // 连接池最大生成连接数
        connectionManager.setMaxTotal(100);
        // 默认设置路由的最大连接数
        connectionManager.setDefaultMaxPerRoute(50);
        //2.设置basic认证凭证
        CREDENTIALS_PROVIDER.setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
                new UsernamePasswordCredentials("", ""));
        //3.初始化HTTP请求配置
        REQUEST_CONFIG = RequestConfig.custom()
                // 是否启用内容压缩 默认为true
                .setContentCompressionEnabled(true)
                // 确定是否应自动处理身份验证
                .setAuthenticationEnabled(true)
                // socket 读写超时时间
                .setSocketTimeout(100)
                // 请求超时时间 单位毫秒
                .setConnectionRequestTimeout(100)
                // 连接超时时间 单位毫秒
                .setConnectTimeout(100)
                /* 使用setStaleConnectionCheckEnabled方法来逐出已被关闭的链接不被推荐。更好的方式是手动启用一个线程，
                 * 定时运行closeExpiredConnections和closeIdleConnections方法。
                 * 已过时, 被PoolingHttpClientConnectionManager中的#setValidateAfterInactivity(int)替换掉了
                 */
//                .setStaleConnectionCheckEnabled(true)
                .build();
        //4.创建http客户端
        HTTP_CLIENT = HttpClients.custom()
                // 是否读取系统属性
                .useSystemProperties()
                .setConnectionManager(connectionManager)
                // 目前是默认实现所以可以注释
//                .setKeepAliveStrategy(myStrategy)
                .setDefaultCredentialsProvider(CREDENTIALS_PROVIDER)
                .setDefaultRequestConfig(REQUEST_CONFIG)
                // 重试, 默认重试3次
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                .build();
        //创建异步httpClient
        HTTP_ASYNC_CLIENT = HttpAsyncClients.custom()
                .useSystemProperties()
                // 全局最大维持的连接数
                .setMaxConnTotal(50)
                .setDefaultCredentialsProvider(CREDENTIALS_PROVIDER).build();
        //异步请求开始
        HTTP_ASYNC_CLIENT.start();
        //5.初始化response解析器
        RESPONSE_HANDLER = new BasicResponseHandler();
        //初始化httpGet
        HTTP_GET = new HttpGet();
        HTTP_GET.setConfig(REQUEST_CONFIG);
//        HTTP_GET.setParams(builderHttpParams());
    }

    /**
     * 配置请求连接参数
     */
    @Deprecated
    private static HttpParams builderHttpParams() {
        // 配置连接参数
        HttpParams params = new BasicHttpParams();
        //设置请求超时2秒钟 根据业务调整
        params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2 * 1000);
        //设置等待数据超时时间2秒钟 根据业务调整
        params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 2 * 1000);
        /* 定义了当从ClientConnectionManager中检索ManagedClientConnection实例时使用的毫秒级的超时时间这个参数期望得到一个
         *  java.lang.Long类型的值。如果这个参数没有被设置，默认等于CONNECTION_TIMEOUT，因此一定要设置。
         * 在httpclient4.2.3中我记得它被改成了一个对象导致直接用long会报错，后来又改回来了
         */
        params.setLongParameter(ClientPNames.CONN_MANAGER_TIMEOUT, 500L);
        //在提交请求之前 测试连接是否可用
        params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, true);
        return params;
    }

    // 定义一个线程将已关闭的连接
    @Deprecated
    public static class CustomConnectionMonitorThread extends Thread {

        private final HttpClientConnectionManager connMgr;
        private volatile boolean shutdown;

        public CustomConnectionMonitorThread(HttpClientConnectionManager connMgr) {
            super();
            this.connMgr = connMgr;
        }

        @Override
        public void run() {
            // 关闭过期和空闲连接
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(5000);
                        //关闭过期连接
                        connMgr.closeExpiredConnections();
                        //关闭空闲连接
                        connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
                        shutdown();
                    }
                }
            } catch (InterruptedException ex) {
                // terminate
            }
        }

        public void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }

    /*
     * 发送http请求
     */
    public static String getResponse(String url) throws IOException {
        HTTP_GET.setURI(URI.create(url));
        return HTTP_CLIENT.execute(HTTP_GET, RESPONSE_HANDLER);
    }

    /*
     * 发送http请求，并用net.sf.json工具解析
     */
    public static JSONObject getUrl(String url) throws Exception {
        try {
            HTTP_GET.setURI(URI.create(url));
            String response = HTTP_CLIENT.execute(HTTP_GET, RESPONSE_HANDLER);
            return JSONObject.fromObject(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 发送http请求，并用jackson工具解析
     */
    public static JsonNode getUrl2(String url) {
        try {
            HTTP_GET.setURI(URI.create(url));
            String response = HTTP_CLIENT.execute(HTTP_GET, RESPONSE_HANDLER);
            return MAPPER.readTree(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 发送http请求，并用fastjson工具解析
     */
    public static com.alibaba.fastjson2.JSONObject getUrl3(String url) {
        try {
            HTTP_GET.setURI(URI.create(url));
            String response = HTTP_CLIENT.execute(HTTP_GET, RESPONSE_HANDLER);
            return com.alibaba.fastjson2.JSONObject.parseObject(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 异步获取结果
     */
    public static Future<HttpResponse> asyncGetUrl(String url) {
        HTTP_GET.setURI(URI.create(url));
        return HTTP_ASYNC_CLIENT.execute(HTTP_GET, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                // 自定义请求完成逻辑
            }

            @Override
            public void failed(Exception e) {
                // 自定义请求失败逻辑
            }

            @Override
            public void cancelled() {
                // 自定义请求取消逻辑
            }
        });
    }
}