package org.hf.boot.springboot.proxy.servlet;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIUtils;
import org.hf.boot.springboot.error.BusinessException;
import org.hf.boot.springboot.proxy.properties.ProxyServletProperties;
import org.hf.boot.springboot.utils.SpringContextUtil;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p> 代理Servlet抽象类 </p >
 * 请求代理器实现 - 3
 * 继承HttpServlet实现AbstractProxyServlet ,根据ProxyServletProperties维护本地路径-目标路径的映射，同时重写service方法实现目标路径查询和请求/响应体转换;
 * @author HUFEI
 * @date 2023-06-05
 **/
@Slf4j
public abstract class AbstractProxyServlet extends HttpServlet {

    /**
     * 服务配置
     */
    protected final ProxyServletProperties servletProperties;

    public static final String PROXY_SERVER_NAME = "proxyServerName";

    /**
     * location配置map
     */
    private final Map<String,ProxyServletProperties.ProxyServletLocationProperties> pathLocationMap;

    private final Map<String,ProxyServletProperties.ProxyServletServerProperties> serverLocationMap;

    protected static final String ATTR_TARGET_URI = AbstractProxyServlet.class.getSimpleName() + ".targetUri";
    protected static final String ATTR_TARGET_HOST = AbstractProxyServlet.class.getSimpleName() + ".targetHost";

    public AbstractProxyServlet(){
        ProxyServletProperties proxyServletProperties = SpringContextUtil.getBean(ProxyServletProperties.class);
        this.servletProperties = proxyServletProperties;
        pathLocationMap = new HashMap<>();
        serverLocationMap = new HashMap<>();
        List<ProxyServletProperties.ProxyServletServerProperties> serverProperties = proxyServletProperties.getServer();
        if(CollectionUtils.isNotEmpty(serverProperties)){
            for (ProxyServletProperties.ProxyServletServerProperties serverProperty : serverProperties) {
                List<ProxyServletProperties.ProxyServletLocationProperties> locationProperties = serverProperty.getLocation();
                if (CollectionUtils.isNotEmpty(locationProperties)) {
                    for (ProxyServletProperties.ProxyServletLocationProperties locationProperty : locationProperties) {
                        String locationMapKey = serverProperty.getUrlPattern() + locationProperty.getPath();
                        if(!pathLocationMap.containsKey(locationMapKey)){
                            pathLocationMap.put(locationMapKey, locationProperty);
                            serverLocationMap.put(locationMapKey,serverProperty);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
        String path = servletRequest.getServletPath();
        servletRequest.getAttribute(PROXY_SERVER_NAME);
        log.info("DefaultProxyServlet.service 访问servlet path：{}", servletRequest.getServletPath());
        ProxyServletProperties.ProxyServletLocationProperties locationProperties = pathLocationMap.get(path);
        //这里其实也可以不用判断，因为springMVC中的requestMapping已判断
        if (Objects.isNull(locationProperties)) {
            throw new BusinessException("请求地址不存在");
        }
        ProxyServletProperties.ProxyServletServerProperties serverProperties = serverLocationMap.get(path);
        String targetUri = serverProperties.getTargetDomain() + locationProperties.getTargetPath();
        servletRequest.setAttribute(ATTR_TARGET_URI, targetUri);
        URI uri = null;
        try {
            uri = new URI(targetUri);
        } catch (URISyntaxException e) {
            log.error("创建URI对象出错, targetUri[{}]", targetUri, e);
        }
        servletRequest.setAttribute(ATTR_TARGET_HOST, URIUtils.extractHost(uri));
        //后置处理
        postHandler(servletRequest,servletResponse);
    }

    /**
     * 后置处理
     * @param servletRequest    请求对象
     * @param servletResponse   响应对象
     * @throws IOException 异常
     */
    protected void postHandler(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException {
        // TODO 这里待完善不同ContentType的参数转换问题
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(servletRequest);
        Object requestJson = null;
        if ("post".equalsIgnoreCase(servletRequest.getMethod()) && "application/json".equalsIgnoreCase(servletRequest.getContentType()) && StringUtils.isNotBlank(SpringContextUtil.getBodyString(servletRequest))) {
            requestJson = messageConverter.read(JSONObject.class, serverHttpRequest);
        }
        log.info("AbstractProxyServlet.service request body: {}",requestJson);
        //实际处理
        Object responseBody = doDispatch(servletRequest, requestJson);
        log.info("AbstractProxyServlet.service response body: {}",responseBody.toString());
        //结果转responseBody
        ServletServerHttpResponse servletServerHttpResponse = new ServletServerHttpResponse(servletResponse);
        messageConverter.write(responseBody, MediaType.APPLICATION_JSON, servletServerHttpResponse);
    }

    /**
     * 执行转发
     * @param servletRequest  请求对象
     * @param requestJson     请求参数
     * @return Object
     */
    protected abstract Object doDispatch(HttpServletRequest servletRequest, Object requestJson);
}