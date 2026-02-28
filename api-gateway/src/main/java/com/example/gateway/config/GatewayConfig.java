package com.example.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    /**
     * 根据IP地址进行限流
     */
    @Primary
    @Bean(name = "ipKeyResolver")
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
        );
    }

    /**
     * 根据请求路径进行限流
     */
    @Bean(name = "apiKeyResolver")
    public KeyResolver apiKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest().getPath().value()
        );
    }

    /**
     * 根据用户ID进行限流（需要请求中包含userId参数）
     */
    @Bean(name = "userKeyResolver")
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest().getQueryParams().getFirst("userId")
        );
    }
}