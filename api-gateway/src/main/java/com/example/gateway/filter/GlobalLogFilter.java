package com.example.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GlobalLogFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(GlobalLogFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 正确获取HTTP方法的方式
        HttpMethod httpMethod = request.getMethod();
        String methodName = httpMethod != null ? httpMethod.name() : "UNKNOWN";

        // 记录请求信息
        log.info("Gateway请求: {} {}, 客户端IP: {}",
                methodName,  // 这里是正确的方法名
                request.getURI().getPath(),
                request.getRemoteAddress());

        // 可以在这里添加认证、鉴权逻辑

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;  // 过滤器执行顺序，值越小优先级越高
    }
}