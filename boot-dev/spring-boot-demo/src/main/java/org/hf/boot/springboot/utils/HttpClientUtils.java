package org.hf.boot.springboot.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.hf.boot.springboot.error.BusinessException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p> httpClient请求工具类 </p >
 * 请求对象配置参考示例: {@link HttpClientDemoUtils}
 * @author HUFEI
 * @date 2023-06-09
 **/
@Slf4j
public class HttpClientUtils {

    /**
     * 执行http的get请求, 指定响应结果字符集为UTF-8
     * @param url       请求地址
     * @return String
     */
    public static String get(String url) {
        return get(url, "UTF-8");
    }

    /**
     * 执行http的get请求
     * @param url       请求地址
     * @param charset   响应参数字符集
     * @return String
     */
    public static String get(String url, String charset) {
        HttpGet httpGet = new HttpGet(url);
        return executeRequest(httpGet, charset);
    }

    /**
     * 执行http的get请求, 返回byte 需注意文件大小，谨慎使用
     * @param url 请求地址
     * @return byte[]
     */
    public static byte[] getByte(String url) {
        HttpGet httpGet = new HttpGet(url);
        return executeRequestReturnByte(httpGet);
    }

    /**
     * 执行异步http的get请求, 指定响应结果字符集为UTF-8
     * @param url       请求地址
     * @return String
     */
    public static String ajaxGet(String url) {
        return ajaxGet(url, "UTF-8");
    }

    /**
     * 执行异步http的get请求
     * @param url       请求地址
     * @param charset   响应参数字符集
     * @return String
     */
    public static String ajaxGet(String url, String charset) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("X-Requested-With", "XMLHttpRequest");
        return executeRequest(httpGet, charset);
    }

    /**
     * 执行http的post请求, 指定响应结果字符集为UTF-8
     * @param url           请求地址
     * @param dataMap       入参集合
     * @return  String
     */
    public static String post(String url, Map<String, String> dataMap) {
        log.info("http的post请求，传递map格式参数--- dataMap:{},url:{}", JSONObject.toJSONString(dataMap), JSONObject.toJSONString(url));
        return post(url, dataMap, "UTF-8");
    }

    /**
     * 执行http的post请求
     * @param url           请求地址
     * @param dataMap       入参集合
     * @param charset       入参/出参字符集
     * @return  String
     */
    public static String post(String url, Map<String, String> dataMap, String charset) {
        HttpPost httpPost = new HttpPost(url);
        buildParamsMap(dataMap, httpPost, charset);
        return executeRequest(httpPost, charset);
    }

    /**
     * 执行http的post请求, 指定响应结果字符集为UTF-8
     * @param url           请求地址
     * @param jsonString    请求body参数
     * @return String
     */
    public static String postJson(String url, String jsonString) {
        return postJson(url, jsonString, "UTF-8");
    }

    /**
     * 执行http的post请求
     * @param url           请求地址
     * @param jsonString    请求body参数
     * @param charset       响应结果字符集
     * @return String
     */
    public static String postJson(String url, String jsonString, String charset) {
        HttpPost httpPost = new HttpPost(url);
        // 解决中文乱码问题
        StringEntity stringEntity = new StringEntity(jsonString, charset);
        stringEntity.setContentEncoding(charset);
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        return executeRequest(httpPost, charset);
    }

    /**
     * 执行https的post请求
     * @param url           请求地址
     * @param jsonString    请求body参数
     * @param headers       请求头集合
     * @param charset       字符集类型
     * @return String
     */
    public static String postJsonSetHeaders(String url, String jsonString, Map<String, String> headers, String charset) {
        HttpPost httpPost = new HttpPost(url);
        for (String key : headers.keySet()) {
            httpPost.setHeader(key, headers.get(key));
        }
        // 解决中文乱码问题
        StringEntity stringEntity = new StringEntity(jsonString, charset);
        stringEntity.setContentEncoding(charset);
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        return executeRequest(httpPost, charset);
    }

    /**
     * 执行异步http的post请求, 指定响应结果字符集为UTF-8
     * @param url           请求地址
     * @param jsonString    请求body参数
     * @return  String
     */
    public static String ajaxPostJson(String url, String jsonString) {
        return ajaxPostJson(url, jsonString, "UTF-8");
    }

    /**
     * 执行异步http的post请求
     * @param url           请求地址
     * @param jsonString    请求body参数
     * @param charset       响应结果字符集
     * @return  String
     */
    public static String ajaxPostJson(String url, String jsonString, String charset) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("X-Requested-With", "X-Requested-With");
        // 解决中文乱码问题
        StringEntity stringEntity = new StringEntity(jsonString, charset);
        stringEntity.setContentEncoding(charset);
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        return executeRequest(httpPost, charset);
    }

    /**
     * 执行异步http的post请求, 指定出参/入参字符集为UTF-8
     * @param url           请求地址
     * @param dataMap       入参集合
     * @return  String
     */
    public static String ajaxPost(String url, Map<String, String> dataMap) {
        return ajaxPost(url, dataMap, "UTF-8");
    }

    /**
     * 执行异步http的post请求
     * @param url           请求地址
     * @param dataMap       入参集合
     * @param charset       入参/出参字符集
     * @return  String
     */
    public static String ajaxPost(String url, Map<String, String> dataMap, String charset) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
        buildParamsMap(dataMap, httpPost, charset);
        return executeRequest(httpPost, charset);
    }

    /**
     * 组装参数集合
     * @param dataMap   请求入参集合
     * @param httpPost  请求对象
     * @param charset   入参结果字符集
     */
    private static void buildParamsMap(Map<String, String> dataMap, HttpPost httpPost, String charset) {
        try {
            if (dataMap != null) {
                List<NameValuePair> nvps = new ArrayList<>();
                for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps, charset);
                formEntity.setContentEncoding(charset);
                httpPost.setEntity(formEntity);
            }
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 执行一个http请求, 指定请求响应结果字符集编码为UTF-8
     * @param httpRequest 请求对象
     * @return String
     */
    private static String executeRequest(HttpUriRequest httpRequest) {
        return executeRequest(httpRequest, "UTF-8");
    }

    /**
     * 执行一个http请求 自定义响应参数的字符集
     * @param httpRequest   请求对象
     * @param charset       响应结果字符集
     * @return String
     */
    private static String executeRequest(HttpUriRequest httpRequest, String charset) {
        return executeRequest(httpRequest, charset, String.class);
    }

    /**
     * 指定请求响应结果字符集编码为UTF-8, 返回结果为字节数组byte[]
     * @param httpRequest 请求对象
     * @return byte[]
     */
    private static byte[] executeRequestReturnByte(HttpUriRequest httpRequest) {
        return executeRequest(httpRequest, "UTF-8", byte[].class);
    }

    /**
     * 请求调用实现
     * @param httpRequest   请求对象
     * @param charset       响应结果字符集
     * @param clazz         响应结果类型
     * @param <T>           类型
     * @return  T
     */
    private static <T> T executeRequest(HttpUriRequest httpRequest, String charset, Class<T> clazz) {
        CloseableHttpClient httpclient;
        if ("https".equals(httpRequest.getURI().getScheme())) {
            httpclient = createSslInsecureClient();
        } else {
            // 创建Httpclient对象
//            HttpHost proxy = new HttpHost("30.16.105.251", 80, "http");
//            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
//            httpclient = HttpClients.custom().setRoutePlanner(routePlanner).build();
//            HttpHost proxy = new HttpHost("10.37.84.36",8080);
//            CredentialsProvider provider = new BasicCredentialsProvider();
            //包含账号密码的代理
//            provider.setCredentials(new AuthScope(proxy), new UsernamePasswordCredentials("NINGZUOHUA848", "###"));
//            httpclient = HttpClients.custom().setDefaultCredentialsProvider(provider).build();
            httpclient = HttpClients.custom().setDefaultRequestConfig(getRequestConfig()).build();
        }
        T result;
        try {
            try {
                log.info("test--> HttpClientUtils.httpRequest:{}", JSON.toJSONString(httpRequest));
                CloseableHttpResponse response = httpclient.execute(httpRequest);
                log.info("返回结果为 response:{}",JSONObject.toJSONString(response));
                HttpEntity entity = null;
                try {
                    entity = response.getEntity();
                    if (String.class.isAssignableFrom(clazz)) {
                        result = clazz.cast(EntityUtils.toString(entity, charset));
                    } else {
                        result = (T) EntityUtils.toByteArray(entity);
                    }
                    log.info("返回结果为 result:{}",JSONObject.toJSONString(result));
                } finally {
                    EntityUtils.consume(entity);
                    response.close();
                }
            } finally {
                httpclient.close();
            }
        } catch (UnknownHostException ex) {
            throw new BusinessException("未知主机：" + ex.getMessage(), ex);
        } catch (IOException ex) {
            log.error("executeRequest exception", ex);
            throw new BusinessException("执行Http请求出错：" + ex.getMessage(), ex);
        }
        return result;
    }

    /**
     * 创建 SSL连接
     */
    public static CloseableHttpClient createSslInsecureClient() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return HttpClients.custom().setDefaultRequestConfig(getRequestConfig()).setSSLSocketFactory(sslsf).build();
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 配置连接超时参数
     */
    private static RequestConfig getRequestConfig() {
        return RequestConfig.custom().setConnectionRequestTimeout(10000).setSocketTimeout(7000).setConnectTimeout(10000).build();
    }
}