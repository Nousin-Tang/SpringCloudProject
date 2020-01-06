package com.nousin.springcloud.server.storage.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2 配置类
 *
 * @author Nousin
 * @since 2020/1/3
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${nousin.swagger.enable:false}")
    private Boolean swaggerEnable;


    @Bean
    public Docket userApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.nousin.springcloud.server.storage.web.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo()).enable(swaggerEnable);

    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("门店接口")
                .description("NewRetailing门店服务端接口服务")
                .version("1.0.1")
                .build();
    }

}
