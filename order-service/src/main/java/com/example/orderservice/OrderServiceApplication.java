package com.example.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 订单服务启动类
 *
 * @SpringBootApplication: Spring Boot应用标识
 * @EnableDiscoveryClient: 启用服务发现客户端
 * @EnableFeignClients: 启用Feign客户端，指定扫描包路径
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.example.orderservice.feign")
public class OrderServiceApplication {

    /**
     * 应用主入口
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }


}