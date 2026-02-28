package com.example.userservice.service.impl;

import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import com.example.common.response.Result;
import com.example.common.enums.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import java.util.List;
import java.util.Optional;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public Result<User> register(User user) {
        try {
            // 检查用户名是否已存在
            if (userRepository.existsByUsername(user.getUsername())) {
                return Result.error(ResultCode.BAD_REQUEST.getCode(), "用户名已存在");
            }

            // 检查邮箱是否已存在
            if (userRepository.existsByEmail(user.getEmail())) {
                return Result.error(ResultCode.BAD_REQUEST.getCode(), "邮箱已被注册");
            }

            // 加密密码
            String encryptedPassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
            user.setPassword(encryptedPassword);

            // 设置默认昵称
            if (user.getNickname() == null) {
                user.setNickname(user.getUsername());
            }

            User savedUser = userRepository.save(user);
            return Result.success("注册成功", savedUser);

        } catch (Exception e) {
            log.error("用户注册失败: {}", e.getMessage());
            return Result.error("注册失败");
        }
    }

    @Override
    public Result<User> login(String account, String password) {
        try {
            Optional<User> userOpt = userRepository.findByUsernameOrEmail(account);
            if (!userOpt.isPresent()) {
                return Result.error("用户不存在");
            }

            User user = userOpt.get();
            String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());

            if (!user.getPassword().equals(encryptedPassword)) {
                return Result.error("密码错误");
            }

            if (user.getStatus() == 0) {
                return Result.error("账户已被禁用");
            }

            return Result.success("登录成功", user);

        } catch (Exception e) {
            log.error("用户登录失败: {}", e.getMessage());
            return Result.error("登录失败");
        }
    }

    @Override
    public Result<User> getUserById(Long id) {
        try {
            Optional<User> userOpt = userRepository.findById(id);
            if (!userOpt.isPresent()) {
                return Result.error("用户不存在");
            }
            return Result.success(userOpt.get());
        } catch (Exception e) {
            log.error("获取用户信息失败: {}", e.getMessage());
            return Result.error("获取用户信息失败");
        }
    }

    @Override
    public Result<User> getUserByUsername(String username) {
        try {
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (!userOpt.isPresent()) {
                return Result.error("用户不存在");
            }
            return Result.success(userOpt.get());
        } catch (Exception e) {
            log.error("获取用户信息失败: {}", e.getMessage());
            return Result.error("获取用户信息失败");
        }
    }

    @Override
    public Result<List<User>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return Result.success(users);
        } catch (Exception e) {
            log.error("获取用户列表失败: {}", e.getMessage());
            return Result.error("获取用户列表失败");
        }
    }

    @Override
    @Transactional
    public Result<User> updateUser(Long id, User userDetails) {
        try {
            Optional<User> userOpt = userRepository.findById(id);
            if (!userOpt.isPresent()) {
                return Result.error("用户不存在");
            }

            User existingUser = userOpt.get();

            // 更新非空字段
            if (userDetails.getNickname() != null) {
                existingUser.setNickname(userDetails.getNickname());
            }
            if (userDetails.getPhone() != null) {
                existingUser.setPhone(userDetails.getPhone());
            }

            User updatedUser = userRepository.save(existingUser);
            return Result.success("更新成功", updatedUser);

        } catch (Exception e) {
            log.error("更新用户信息失败: {}", e.getMessage());
            return Result.error("更新用户信息失败");
        }
    }

    @Override
    @Transactional
    public Result<Void> deleteUser(Long id) {
        try {
            Optional<User> userOpt = userRepository.findById(id);
            if (!userOpt.isPresent()) {
                return Result.error("用户不存在");
            }

            User user = userOpt.get();
            user.setStatus(0); // 软删除
            userRepository.save(user);

            return Result.success();
        } catch (Exception e) {
            log.error("删除用户失败: {}", e.getMessage());
            return Result.error("删除用户失败");
        }
    }
}