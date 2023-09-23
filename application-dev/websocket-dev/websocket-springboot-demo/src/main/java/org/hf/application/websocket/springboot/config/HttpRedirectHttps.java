package org.hf.application.websocket.springboot.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

/**
 * 集成https认证:http访问重定向https
 * 通过Nginx配置也可以
 * 这里暂时注释掉https重定向配置,如果这里重定向到https,需要改如下两个点:1.前端请求时需要将ws://改成wss://;2.需要将证书放在服务工程的resources目录下
 * @author HF
 */
// @Configuration
public class HttpRedirectHttps {

//    @Value("${http.port}")
    Integer httpPort;
//    @Value("${server.port}")
    Integer httpsPort;


    /**
     * 配置一个TomcatServletWebServerFactory的bean然后添加一个Tomcat中的Connector（监听80端口）,并将请求转发到80端口
     */
//    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(createTomcatConnector());
        return tomcat;

    }

    /**
     * Spring Boot 不支持同时启动 HTTP 和 HTTPS ，为了解决这个问题，我们这里可以配置一个请求转发，当用户发起 HTTP 调用时，自动转发到 HTTPS 上
     */
    public Connector createTomcatConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        //Connector监听的http的默认端口号
        connector.setPort(httpPort);
        connector.setSecure(false);
        //监听到http的端口号后转向到的https的端口号,也就是项目配置的port
        connector.setRedirectPort(httpsPort);
        return connector;
    }
}