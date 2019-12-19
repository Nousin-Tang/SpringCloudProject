package com.nousin.springcloud.server.order.framework.config;

import feign.Feign;
import feign.form.FormEncoder;
import feign.codec.Encoder;
import okhttp3.ConnectionPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Feign Ok Http配置
 *
 * @author tangwc
 * @since 2019/12/19
 */
@AutoConfigureBefore(FeignAutoConfiguration.class)
@Configuration
@ConditionalOnClass(Feign.class)
public class FeignOkHttpConfig {
    //设置连接超时
    @Value("${nousin.feign.ok-http.connect-timeout:60}")
    private int feignConnectTimeout;
    //设置读超时
    @Value("${nousin.feign.ok-http.read-timeout:60}")
    private int feignOkHttpReadTimeout;
    //设置写超时
    @Value("${nousin.feign.ok-http.write-Timeout:120}")
    private int feignWriteTimeout;
    @Bean
    public Encoder encoder() {
        return new FormEncoder();
    }

    @Bean
    public okhttp3.OkHttpClient okHttpClient() {
        return new okhttp3.OkHttpClient.Builder()
                .readTimeout(feignOkHttpReadTimeout, TimeUnit.SECONDS)
                .connectTimeout(feignConnectTimeout, TimeUnit.SECONDS)
                .writeTimeout(feignWriteTimeout, TimeUnit.SECONDS)
                //是否自动重连
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool(10, 5L, TimeUnit.MINUTES))
                .build();
    }


}
