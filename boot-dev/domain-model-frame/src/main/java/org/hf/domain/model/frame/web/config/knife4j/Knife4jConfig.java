package org.hf.domain.model.frame.web.config.knife4j;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * knife4j配置, 默认访问地址: localhost:端口/doc.html
 * 注意:如果配置了拦截器需要考虑是否放行swagger相关的路径
 */
@Configuration
@EnableOpenApi
@EnableKnife4j
@ConditionalOnProperty(prefix = "web", value = "config.swagger.enabled", havingValue = "true")
public class Knife4jConfig {
    @Resource
    private Environment environment;

    public static final String REQUEST_HEADER_SYSTEM_CODE = "SYSTEM-CODE";

    public static final String SWAGGER_UM = "swaggerUserName";

    /**
     * 创建Docket存入容器，Docket代表一个接口文档
     */
    @Bean(value = "defaultApi3")
    public Docket defaultApi3() {
        // 设置需要显示Knife4j的环境
        Profiles profiles = Profiles.of("dev");
        // 判断当前是否处于该环境,通过 enable() 接收此参数判断是否要显示
        boolean isShow = environment.acceptsProfiles(profiles);
        return new Docket(DocumentationType.OAS_30)
                // 创建接口文档的具体信息
                .apiInfo(new ApiInfoBuilder()
                        // 文档标题
                        .title("custom接口文档")
                        // 文档描述
                        .description("custom-service-api")
                        // 联系人信息
                        .contact(new Contact("hf", "org.hf", "email"))
                        // 版本
                        .version("1.0")
                        .build())
                // 分组名称
                .groupName("1.0版本")
                // 是否开启(thue)
                .enable(isShow)
                // 创建选择器，控制哪些接口被加入文档
                .select()
                // 这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("org.hf.domain.model.frame.custom.application.controller.api"))
                // 指定路径处理, PathSelectors.any()表示所有路径
                .paths(PathSelectors.any())
                // 指定@ApiOperation标注的接口被加入文档
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build();
                // 设置默认请求参数
//                .globalRequestParameters(genDefaultGlobalRequestParameters());
    }

    /**
     * 构建默认请求参数
     */
    private List<RequestParameter> genDefaultGlobalRequestParameters() {
        List<RequestParameter> requestParameters = new ArrayList<>();
        // 设置默认参数
        requestParameters.add(new RequestParameterBuilder()
                        .name(REQUEST_HEADER_SYSTEM_CODE)
                        .description("应用端标识")
                        .in(ParameterType.HEADER)
                // 设置参数类型
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                .required(false)
                // 设置参数索引位置
                .parameterIndex(1)
                .build());
        requestParameters.add(new RequestParameterBuilder()
                .name(SWAGGER_UM)
                .description("模拟登录的账号(user_info表的userName)")
                .in(ParameterType.HEADER)
                .query(q -> {
                    q.model(m -> m.scalarModel(ScalarType.STRING));
                    q.defaultValue("hf").allowEmptyValue(true);
                })
                .required(false)
                .parameterIndex(2)
                .build());
        return requestParameters;
    }

    /**
     * 找到所有的@Endpoint标记的服务端点注册到springMVC中,交给路由转发
     * swagger3.0兼容springBoot 2.6.x以上配置, 如果依赖了actuator, 这段配置必须加上,否则可以不加, 建议最好加上不然会出现一些请求无法识别
     */
    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
    }

    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }
}