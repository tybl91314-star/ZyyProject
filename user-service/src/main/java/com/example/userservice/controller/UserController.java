package com.example.userservice.controller;

import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;
import com.example.common.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
 * 用户控制器
 * 提供REST API接口
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody User user) {
        log.info("用户注册请求: {}", user.getUsername());
        return userService.register(user);
    }

    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public Result<User> login(@RequestParam String account,
                              @RequestParam String password) {
        log.info("用户登录请求: {}", account);
        return userService.login(account, password);
    }

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        log.info("查询用户信息: {}", id);
        return userService.getUserById(id);
    }

    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/username/{username}")
    public Result<User> getUserByUsername(@PathVariable String username) {
        log.info("查询用户信息: {}", username);
        return userService.getUserByUsername(username);
    }

    /**
     * 获取所有用户列表
     */
    @GetMapping
    public Result<List<User>> getAllUsers() {
        log.info("查询所有用户列表");
        return userService.getAllUsers();
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable Long id,
                                   @RequestBody User userDetails) {
        log.info("更新用户信息: {}", id);
        return userService.updateUser(id, userDetails);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        log.info("删除用户: {}", id);
        return userService.deleteUser(id);
    }
}