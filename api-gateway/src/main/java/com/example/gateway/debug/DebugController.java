package com.example.gateway.debug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/debug")
public class DebugController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private ApplicationContext applicationContext;  // 添加ApplicationContext注入

    /**
     * 响应式版本 - 获取服务实例信息
     * 使用Mono.just()而不是fromCallable()
     */
    @GetMapping("/refresh-instances")
    public Mono<String> refreshInstances() {
        return Mono.just(getServiceInstancesInfo());
    }

    /**
     * 获取所有注册的服务
     */
    @GetMapping("/all-services")
    public Mono<String> getAllServices() {
        return Mono.just(getAllServicesInfo());
    }

    /**
     * 比较所有服务实例
     */
    @GetMapping("/compare-services")
    public Mono<String> compareServices() {
        return Mono.just(compareAllServices());
    }

    /**
     * 阻塞操作封装在单独的方法中
     */
    private String getServiceInstancesInfo() {
        try {
            List<ServiceInstance> instances = discoveryClient.getInstances("order-service");

            if (instances.isEmpty()) {
                return "❌ No instances found for order-service!\n" +
                        "Available services: " + discoveryClient.getServices();
            }

            StringBuilder result = new StringBuilder();
            result.append("✅ Found ").append(instances.size()).append(" instances for order-service:\n");

            for (ServiceInstance instance : instances) {
                result.append(" - Service: ").append(instance.getServiceId()).append("\n")
                        .append("   URI: ").append(instance.getUri()).append("\n")
                        .append("   Host: ").append(instance.getHost()).append("\n")
                        .append("   Port: ").append(instance.getPort()).append("\n")
                        .append("   Metadata: ").append(instance.getMetadata()).append("\n\n");
            }

            return result.toString();
        } catch (Exception e) {
            return "❌ Error getting instances: " + e.getMessage();
        }
    }

    private String getAllServicesInfo() {
        try {
            List<String> services = discoveryClient.getServices();
            StringBuilder result = new StringBuilder("Registered services: " + services.size() + "\n");

            for (String service : services) {
                List<ServiceInstance> instances = discoveryClient.getInstances(service);
                result.append(" - ").append(service).append(": ").append(instances.size()).append(" instances\n");
            }

            return result.toString();
        } catch (Exception e) {
            return "❌ Error getting services: " + e.getMessage();
        }
    }

    private String compareAllServices() {
        try {
            StringBuilder result = new StringBuilder();
            List<String> services = discoveryClient.getServices();

            for (String service : services) {
                List<ServiceInstance> instances = discoveryClient.getInstances(service);
                result.append("Service: ").append(service)
                        .append(" | Instances: ").append(instances.size())
                        .append(" | Status: ").append(instances.isEmpty() ? "❌" : "✅")
                        .append("\n");

                for (ServiceInstance instance : instances) {
                    result.append("  └─ ").append(instance.getUri())
                            .append(" (Metadata: ").append(instance.getMetadata()).append(")\n");
                }
            }

            return result.toString();
        } catch (Exception e) {
            return "❌ Error comparing services: " + e.getMessage();
        }
    }

    @GetMapping("/check-nacos-config")
    public Mono<String> checkNacosConfig() {
        return Mono.fromSupplier(() -> {
            // 获取当前Nacos配置
            Environment env = applicationContext.getEnvironment();
            String namespace = env.getProperty("spring.cloud.nacos.discovery.namespace");
            String group = env.getProperty("spring.cloud.nacos.discovery.group");

            return "当前配置:\n" +
                    "命名空间(namespace): " + (namespace == null ? "默认(public)" : namespace) + "\n" +
                    "分组(group): " + (group == null ? "默认(DEFAULT_GROUP)" : group);
        });
    }

}