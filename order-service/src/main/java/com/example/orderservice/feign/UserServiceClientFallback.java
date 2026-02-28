package com.example.orderservice.feign;

import com.example.common.dto.UserDTO;
import com.example.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户服务Feign客户端降级处理
 * 当user-service不可用时提供默认响应
 */
@Slf4j
@Component
public class UserServiceClientFallback implements UserServiceClient {

    @Override
    public Result<UserDTO> getUserById(Long id) {
        log.warn("用户服务不可用，getUserById降级处理，用户ID: {}", id);
        return Result.error(503, "用户服务暂时不可用");
    }

    @Override
    public Result<UserDTO> getUserByUsername(String username) {
        log.warn("用户服务不可用，getUserByUsername降级处理，用户名: {}", username);
        return Result.error(503, "用户服务暂时不可用");
    }

    @Override
    public Result<Boolean> checkUserExists(String username, String email) {
        log.warn("用户服务不可用，checkUserExists降级处理");
        return Result.error(503, "用户服务暂时不可用");
    }
}