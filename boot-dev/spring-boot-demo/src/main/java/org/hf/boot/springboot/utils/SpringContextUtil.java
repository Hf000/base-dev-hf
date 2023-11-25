package org.hf.boot.springboot.utils;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Objects;
import org.hf.boot.springboot.error.BusinessException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p> Spring Context 工具类 </p >
 *
 * @author HUFEI
 * @date 2023-06-05
 **/
@Slf4j
@Component
@ConditionalOnClass(ApplicationContext.class)
public class SpringContextUtil implements ApplicationContextAware, EnvironmentAware {

    /**
     * 容器上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * 配置变量信息
     */
    private static Environment environment;

    private static void setCtx(ApplicationContext applicationContext) {
        if (SpringContextUtil.applicationContext == null) {
            SpringContextUtil.applicationContext = applicationContext;
        }
    }

    private static void setEnv(Environment env) {
        if (SpringContextUtil.environment == null) {
            SpringContextUtil.environment = env;
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        setCtx(applicationContext);
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        setEnv(environment);
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    public static Class<?> getType(String name) {
        return applicationContext.getType(name);
    }

    private static final String[] CHARS = new String[]{"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    public static String getUuid20() {
        StringBuilder shortBuffer = new StringBuilder();
        shortBuffer.append("CUSTOM_");
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 16; i++) {
            String str = uuid.substring(i * 2, i * 2 + 2);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(CHARS[x % 36]);
        }
        return shortBuffer.toString();
    }

    public static boolean isPrdEnv() {
        Environment environment = applicationContext.getEnvironment();
        String[] activeProfiles = environment.getActiveProfiles();
        for (String ap : activeProfiles) {
            if ("prd".equals(ap)) {
                log.info("isPrdEnv:{}", ap);
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前线程请求对象<br/>
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes != null ? servletRequestAttributes.getRequest() : null;
    }

    /**
     * 获取当前线程响应对象<br/>
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes != null ? servletRequestAttributes.getResponse() : null;
    }

    /**
     * 获取实现类的bean实例<br/>
     *
     * @param requiredType 类型
     * @return T
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> requiredType) {
        return applicationContext.getBeansOfType(requiredType);
    }

    public static String getProperty(String configKey) {
        return environment.getProperty(configKey);
    }

    public static <T> T registerBean(String name, Class<T> clazz, Object... args) {
        if (SpringContextUtil.containsBean(name)) {
            Object bean = getBean(name);
            if (bean.getClass().isAssignableFrom(clazz)) {
                return Objects.castIfBelongsToType(bean, clazz);
            } else {
                throw new BusinessException("beanName重复, name:" + name);
            }
        }
        if (!(applicationContext instanceof BeanDefinitionRegistry)) {
            throw new BusinessException("applicationContext 不支持runtime注入bean");
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        for (Object arg : args) {
            beanDefinitionBuilder.addConstructorArgValue(arg);
        }
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        ((BeanDefinitionRegistry) applicationContext).registerBeanDefinition(name, beanDefinition);
        return Objects.castIfBelongsToType(getBean(name), clazz);
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

    public static String getCloneBodyString(HttpServletRequest request) throws IOException {
        ByteArrayOutputStream baos = cloneInputStream(request.getInputStream());
        if (baos != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray()), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            br.close();
            return sb.toString();
        }
        return null;
    }

    private static ByteArrayOutputStream cloneInputStream(InputStream input) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 8];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}