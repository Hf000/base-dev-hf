package org.hf.boot.springboot.proxy.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.List;

/**
 * <p> 代理servlet实体配置 </p >
 * 请求代理器实现 - 4
 * @author HUFEI
 * @date 2023-06-05
 **/
@Data
@ConfigurationProperties("proxy.servlet")
@EnableConfigurationProperties(ProxyServletProperties.class)
public class ProxyServletProperties {

    public ProxyServletProperties() {
    }

    /**
     * 开关 true:打开 false:关闭
     */
    private Boolean open;

    /**
     * 服务配置
     */
    private List<ProxyServletServerProperties> server;

    @Data
    public static class ProxyServletServerProperties {

        /**
         * 服务名称
         */
        private String name;

        /**
         * url前缀路径
         */
        private String urlPattern;

        /**
         * 目标服务域名
         */
        private String targetDomain;

        /**
         * 代理路由配置
         */
        private List<ProxyServletLocationProperties> location;

        /**
         * ProxyServlet类
         */
        private String proxyClass;
    }

    @Data
    public static class ProxyServletLocationProperties {
        /**
         * 本地路径
         */
        private String path;
        /**
         * 目标路径
         */
        private String targetPath;
    }
}