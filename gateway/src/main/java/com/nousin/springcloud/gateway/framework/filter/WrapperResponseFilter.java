package com.nousin.springcloud.gateway.framework.filter;

import com.alibaba.fastjson.JSON;
import com.nousin.springcloud.common.dto.ResultDto;
import com.nousin.springcloud.common.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sun.jvm.hotspot.runtime.Bytes;

import java.nio.charset.StandardCharsets;

/**
 * 返回数据的包装类
 * TODO 下载文件时的解决方法
 *
 * @author Nousin
 * @since 2019/12/9
 */
@Component
@Order(-200) // 需要注意的是order需要小于-1，需要先于NettyWriteResponseFilter过滤器执行。
@Slf4j
public class WrapperResponseFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // replace response with decorator
        return chain.filter(exchange.mutate().response(getResponseDecorator(exchange.getResponse())).build());
    }

    /**
     * 获取Response装饰器
     * @param originalResponse 原Response
     * @return 装饰过的Response
     */
    public ServerHttpResponseDecorator getResponseDecorator(ServerHttpResponse originalResponse){
        return new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                final MediaType contentType = super.getHeaders().getContentType();
                log.info("ContentType:{}", contentType.getType());
                if(body instanceof ResultDto){
                    return super.writeWith(body);
                } else if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.map(dataBuffer -> {
                        // probably should reuse buffers
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        // 释放掉内存
                        DataBufferUtils.release(dataBuffer);
                        String rs = new String(content, StandardCharsets.UTF_8);

                        // 尝试转换rs
                        byte[] newRs = null;
                        try{
                            ResultDto resultDto = JSON.parseObject(rs, ResultDto.class);
                            newRs = JSON.toJSONString(resultDto).getBytes(StandardCharsets.UTF_8);
                        } catch (Exception e){
                            newRs = JSON.toJSONString(ResultUtil.error(rs)).getBytes(StandardCharsets.UTF_8);
                        }
                        originalResponse.getHeaders().setContentLength(newRs.length);//如果不重新设置长度则收不到消息。
                        return originalResponse.bufferFactory().wrap(newRs);
                    }));
                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
    }
}
