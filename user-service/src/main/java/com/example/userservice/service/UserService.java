package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.common.response.Result;
import java.util.List;

/**
 * 用户服务接口
 * 定义用户相关的业务方法
 */
public interface UserService {

    /**
     * 用户注册
     */
    Result<User> register(User user);

    /**
     * 用户登录
     */
    Result<User> login(String account, String password);

    /**
     * 根据ID查找用户
     */
    Result<User> getUserById(Long id);

    /**
     * 根据用户名查找用户
     */
    Result<User> getUserByUsername(String username);

    /**
     * 获取所有用户
     */
    Result<List<User>> getAllUsers();

    /**
     * 更新用户信息
     */
    Result<User> updateUser(Long id, User userDetails);

    /**
     * 删除用户
     */
    Result<Void> deleteUser(Long id);
}