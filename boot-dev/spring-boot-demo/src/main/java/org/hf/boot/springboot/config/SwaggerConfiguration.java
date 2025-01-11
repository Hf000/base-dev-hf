package org.hf.boot.springboot.config;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.EndpointLinksResolver;
import org.springframework.boot.actuate.endpoint.web.EndpointMapping;
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes;
import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;
import org.springframework.boot.actuate.endpoint.web.WebEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p> swagger配置类 </p>
 * //@EnableOpenApi  // 不加默认也会启动, 也可在启动类上标记
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/13 9:13
 */
@Slf4j
@Configuration
@EnableOpenApi
public class SwaggerConfiguration {

    /**
     * 扫描的包路径
     */
    private static final  String BASE_PACKAGE = "org.hf.boot.springboot.controller";

    @Bean
    public Docket createRestApi() {
        return generatorDocket(createApiInfo());
    }

    /**
     * 生成文档信息对象
     * @param apiInfo api标识信息
     * @return 文档信息对象
     */
    private Docket generatorDocket(ApiInfo apiInfo) {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo)
                // 开启swagger文档, 也可以在yaml文件中配置
                .enable(true)
                .select()
                // 扫描指定的包路径, 会将扫描到的类下的所有方法作为api接口展示在文档中, 排除标记@ApiIgnore的类
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                // 扫描标记指定注解类型的方法为Api接口生成文档
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 指定路径处理, PathSelectors.any()表示所有路径
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 创建API信息
     * @return Api信息对象
     */
    private ApiInfo createApiInfo() {
        return new ApiInfoBuilder()
                // 设置文档标题
                .title("测试")
                // 文档描述
                .description("接口说明")
                // 创建人信息
                .contact(new Contact("hf", "org.hf", ""))
                // 详细描述
                .termsOfServiceUrl("NO terms of service")
                .license("The Apache License, Version 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                // 版本号
                .version("1.0.0")
                .build();
    }

    /**
     * 处理springboot整合swagger报空指针的问题,设置spring和spring actuator不设置默认的pathPatternsCondition，而是使用ant_path_matcher
     * 如果依赖了actuator,这段必须加上,否则可不加,只需加上spring.mvc.pathmatch.matching-strategy=ant_path_matcher
     */
    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier,
                                                                         ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier,
                                                                         EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties,
                                                                         WebEndpointProperties webEndpointProperties, Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping =
                webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath)
                        || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes,
                corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath),
                shouldRegisterLinksMapping, null);
    }

}
