package com.nousin.springcloud.server.storage.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;

/**
 * WebMvcConfig 配置类
 *
 * @see WebMvcConfigurer
 * @author Nousin
 * @since 2019/12/18
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${nousin.userfiles.basedir}")
    private String basedir;

    @Value("${nousin.userfiles.basedirTemp}")
    private String basedirTemp;

    @Value("${nousin.swagger.enable:false}")
    private Boolean swaggerEnable; // 启用Swagger

    /**
     * 拦截器注册
     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authInterceptor).addPathPatterns("/**");
//    }


    /**
     * 静态资源访问配置
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // swagger
        if (swaggerEnable) {
            registry.addResourceHandler("/swagger-ui.html")
                    .addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
        // 静态资源
        registry.addResourceHandler("/static/**").addResourceLocations("file:" + basedir + File.separator);
    }

    /**
     * 资源跨域访问配置
     */
    @Bean
    public CorsFilter corsFilter() {
        // 跨域配置类
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*"); // 允许任何的head头部
        corsConfiguration.addAllowedOrigin("*"); // 允许任何域名使用
        corsConfiguration.addAllowedMethod("*"); // 允许任何的请求方法
        corsConfiguration.setMaxAge(86400L);
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addExposedHeader("Content-Disposition");// 文件下载时将下载文件信息暴露给前端
        // 资源跨域配置
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    /**
     * 让Spring容器自动代理Options请求
     * 跨域时起作用
     */
    @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setDispatchOptionsRequest(true);
        return dispatcherServlet;
    }

    /**
     * 格式化json类
     */
    @Bean
    public ObjectMapper getObjectMapper() {
        return new JsonMapper();
    }

    /**
     * 内置的multipart支持来处理Web应用程序文件上传
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(basedir);
        return factory.createMultipartConfig();
    }

    /**
     * 自定义校验器
     *
     * @return 校验器
     */
    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                //只要出现校验失败的情况，就立即结束校验，不再进行后续的校验。
                .failFast(true)
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }

    /**
     * 方法校验后置处理器
     *
     * @return 方法校验器
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
        methodValidationPostProcessor.setValidatedAnnotationType(Validated.class);
        methodValidationPostProcessor.setValidator(validator());
        return methodValidationPostProcessor;
    }

}
