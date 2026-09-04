package com.fzx.drivertrainingmanagement.service;

import com.fzx.drivertrainingmanagement.entity.User;
import com.fzx.drivertrainingmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * UserService：
 * 负责处理用户相关业务逻辑
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 查询全部用户
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 根据 id 查询单个用户
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 新增用户
     */
    public User addUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }
        return userRepository.save(user);
    }

    /**
     * 修改用户
     */
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Optional<User> existingUser = userRepository.findByUsername(userDetails.getUsername());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
            throw new RuntimeException("用户名已存在");
        }

        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setRealName(userDetails.getRealName());
        user.setRole(userDetails.getRole());
        user.setStatus(userDetails.getStatus());

        return userRepository.save(user);
    }

    /**
     * 删除用户
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * 条件查询用户
     */
    public List<User> searchUsers(String username, String role, String status) {
        if (username != null && !username.isEmpty()) {
            return userRepository.findByUsernameContaining(username);
        }

        if (role != null && !role.isEmpty()) {
            return userRepository.findByRole(role);
        }

        if (status != null && !status.isEmpty()) {
            return userRepository.findByStatus(status);
        }

        return userRepository.findAll();
    }

    /**
     * 登录校验
     * 规则：
     * 1. 用户名必须存在
     * 2. 密码必须正确
     * 3. 账号状态必须为“启用”
     */
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户名不存在"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }

        if (user.getStatus() == null || !"启用".equals(user.getStatus())) {
            throw new RuntimeException("账号已禁用，无法登录");
        }

        return user;
    }
}