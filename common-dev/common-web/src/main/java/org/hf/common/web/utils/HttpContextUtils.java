package org.hf.common.web.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p> 获取请求相关信息工具类 </p >
 *
 * @author hufei
 * @date 2022-09-05
 **/
public class HttpContextUtils {

    /**
     * 获取当前请求对象
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        // 获取当前请求的域对象,从而获取当前请求对象
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public static String getDomain() {
        HttpServletRequest request = getHttpServletRequest();
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
    }

    /**
     * 获取当前请求的相对路径
     *
     * @return String
     */
    public static String getUri() {
        HttpServletRequest request = getHttpServletRequest();
        return request.getRequestURI();
    }

    public static String getContext() {
        HttpServletRequest request = getHttpServletRequest();
        return request.getContextPath();
    }

    public static String getServletPath() {
        HttpServletRequest request = getHttpServletRequest();
        return request.getServletPath();
    }

    /**
     * 获取请求Body
     */
    public static String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 读取请求行参数
     */
    public static Map<String, Object> readParameters(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            Object value = request.getParameter(parameterName);
            params.put(parameterName, value);
        }
        return params;
    }

    /**
     * 读取请求行参数,拼接成字符串
     */
    public static String readQueryStringParameters(HttpServletRequest request) {
        Map<String, Object> parametersMap = readParameters(request);
        StringBuilder sb = new StringBuilder();
        parametersMap.forEach((k, v) -> sb.append("&").append(k).append("=").append(v));
        String queryStringParameters = sb.toString();
        if (queryStringParameters.startsWith("&")) {
            return queryStringParameters.substring(1);
        }
        return queryStringParameters;
    }

}