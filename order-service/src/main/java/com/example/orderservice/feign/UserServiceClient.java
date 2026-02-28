package com.example.orderservice.feign;

import com.example.common.dto.UserDTO;
import com.example.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务Feign客户端
 * 用于调用user-service的用户相关接口
 *
 * @FeignClient参数说明：
 * - name: 服务名称，对应Nacos中的服务名
 * - path: 统一路径前缀，避免在每个方法上重复写
 * - fallback: 服务降级
 */
@FeignClient(
        name = "user-service",
        path = "/api/users",
        fallback = UserServiceClientFallback.class
)
public interface UserServiceClient {

    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    Result<UserDTO> getUserById(@PathVariable("id") Long id);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/username/{username}")
    Result<UserDTO> getUserByUsername(@PathVariable("username") String username);

    /**
     * 检查用户是否存在
     *
     * @param username 用户名（可选）
     * @param email 邮箱（可选）
     * @return 是否存在
     */
    @GetMapping("/check-exists")
    Result<Boolean> checkUserExists(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email
    );
}