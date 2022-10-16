package org.hf.application.custom.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
//                   当前包路径
                .apis(RequestHandlerSelectors.basePackage("org.hf.application.custom.order.controller"))
                .paths(PathSelectors.any()).build();
    }

    /**
     * 构建api文档的详细信息函数
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("多线程demo")
                //创建人
                .contact(new Contact("Shawn", null, ""))
                //版本号
                .version("1.0")
                //描述
                .description("提供给前端页面调用的相关接口")
                .build();
    }
}
