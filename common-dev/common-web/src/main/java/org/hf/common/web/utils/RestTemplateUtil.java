package org.hf.common.web.utils;

import com.alibaba.fastjson2.JSON;
import com.google.common.base.Charsets;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p> RestTemplateUtil </p >
 * RestTemplate的http请求工具类
 * @author HUFEI
 * @date 2023-06-06
 **/
public class RestTemplateUtil {
    
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    // RestTemplate初始化设置
    static {
        List<HttpMessageConverter<?>> list = REST_TEMPLATE.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : list) {
            if (httpMessageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(Charsets.UTF_8);
                break;
            }
        }
        // 不要改这里，涉及业务逻辑,要设置超时，被调方宕机时，连接会被耗尽，导致应用不可用
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        // 采用httpClient方式远程调用https
//        requestFactory.setHttpClient(HttpClientUtils.createSslInsecureClient());
        requestFactory.setReadTimeout(7000);
        requestFactory.setConnectTimeout(10000);
        requestFactory.setConnectionRequestTimeout(10000);
        REST_TEMPLATE.setRequestFactory(requestFactory);
    }
    
    private RestTemplateUtil() {
    }

    /**
     * GET请求 参数封装在URL中<br/>
     *
     * @param urlWithParam 带参数的URL
     * @return 结果json字符串
     */
    public static String getJson(String urlWithParam) {
        HttpEntity<String> requestEntity = new HttpEntity<>(null, genJsonHeaders(null));
        ResponseEntity<String> response = REST_TEMPLATE.exchange(urlWithParam, HttpMethod.GET, requestEntity, String.class);
        return response.getBody();
    }

    /**
     * GET请求 在URL中预留占位<br/>
     *
     * @param url url?param1={param1}&param2={param2}...
     * @return 结果json字符串
     */
    public static String getJson(String url, Map<String, ?> uriVariables) {
        HttpEntity<String> requestEntity = new HttpEntity<>(null, genJsonHeaders(null));
        ResponseEntity<String> response = REST_TEMPLATE.exchange(url, HttpMethod.GET, requestEntity, String.class, uriVariables);
        return response.getBody();
    }

    /**
     * GET请求 在URL中预留占位<br/>
     *
     * @param url url?param1={param1}&param2={param2}...
     * @return 结果json字符串
     */
    public static String getJson(String url, HttpHeaders httpHeaders, Map<String, ?> uriVariables) {
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = REST_TEMPLATE.exchange(url, HttpMethod.GET, requestEntity, String.class, uriVariables);
        return response.getBody();
    }

    /**
     * POST请求
     * @param url   url
     * @param param 参数
     * @return 结果json字符串
     */
    public static String postJson(String url, Object param) {
        return exchange(url, HttpMethod.POST, param);
    }

    /**
     * POST请求
     * @param url      请求地址
     * @param param     请求参数
     * @return  响应结果
     */
    public static String postString(String url, String param) {
        // 这里第三个参数是响应结果的类型
        return REST_TEMPLATE.postForObject(url, param, String.class);
    }

    /**
     * POST请求
     * @param url     url
     * @param headers 请求头
     * @param param   参数
     * @return 结果json字符串
     */
    public static String postJson(String url, HttpHeaders headers, Object param) {
        return exchange(url, HttpMethod.POST, headers, param);
    }

    /**
     * PUT请求
     * @param url   url
     * @param param 参数
     * @return 结果json字符串
     */
    public static String putJson(String url, Object param) {
        return exchange(url, HttpMethod.PUT, param);
    }

    /**
     * 自定义请求类型POST GET ...
     * @param url    url
     * @param method HttpMethod
     * @param param  参数
     * @return 结果json字符串
     */
    private static String exchange(String url, HttpMethod method, Object param) {
        return exchange(url, method, genJsonHeaders(null), param);
    }

    /**
     * 自定义请求类型POST GET ...
     * @param url     url
     * @param method  HttpMethod
     * @param headers 请求头
     * @param param   参数
     * @return 结果json字符串
     */
    private static String exchange(String url, HttpMethod method, HttpHeaders headers, Object param) {
        String jsonString = JSON.toJSONString(param);
        headers = headers != null ? headers : genJsonHeaders(null);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);
        ResponseEntity<String> response = REST_TEMPLATE.exchange(url, method, requestEntity, String.class);
        return response.getBody();
    }

    /**
     * http请求
     * @param url       请求地址
     * @param method    请求方式
     * @param headers   请求头
     * @param params    请求参数
     * @return String
     */
    public static String doHttpExchange(String url, HttpMethod method, HttpHeaders headers, Object params) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> resp = REST_TEMPLATE.exchange(url, method, requestEntity, String.class);
        return resp.getBody();
    }

    /**
     * form表单提交
     * @param url       请求地址
     * @param params    请求参数
     * @return  String
     */
    public static String postForm(String url, MultiValueMap<String, Object> params) {
        return postForm(url, genFormUrlEncodedHeaders(null), params);
    }

    /**
     * POST表单数据<br/>
     */
    public static String postForm(String url, HttpHeaders httpHeaders, MultiValueMap<String, Object> params) {
        //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        /* MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();*/
        //  也支持中文
        /* params.add("username", "用户名");*/
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, httpHeaders);
        //  执行HTTP请求
        ResponseEntity<String> response = REST_TEMPLATE.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return response.getBody();
    }

    public static HttpHeaders genFormUrlEncodedHeaders() {
        return genFormUrlEncodedHeaders(null);
    }

    /**
     * 生成表单请求头
     * @param headersMap    头信息
     * @return  HttpHeaders
     */
    public static HttpHeaders genFormUrlEncodedHeaders(Map<String, String> headersMap) {
        HttpHeaders httpHeaders = new HttpHeaders();
        // 请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        buildHeadersParam(httpHeaders, headersMap);
        return httpHeaders;
    }

    public static HttpHeaders genJsonHeaders() {
        return genJsonHeaders(null);
    }

    public static HttpHeaders genJsonHeaders(Map<String, String> headersMap) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        buildHeadersParam(httpHeaders, headersMap);
        return httpHeaders;
    }

    private static void buildHeadersParam(HttpHeaders httpHeaders, Map<String, String> headersMap) {
        if (!CollectionUtils.isEmpty(headersMap)) {
            headersMap.forEach(httpHeaders::add);
        }
    }
}