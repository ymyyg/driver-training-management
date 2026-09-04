package com.fzx.drivertrainingmanagement.controller;

import com.fzx.drivertrainingmanagement.common.Result;
import com.fzx.drivertrainingmanagement.entity.User;
import com.fzx.drivertrainingmanagement.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * UserController：
 * 负责处理用户管理相关接口
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 查询全部用户
     */
    @GetMapping
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        if (users == null || users.isEmpty()) {
            return Result.success("暂无用户数据", users);
        }

        return Result.success("查询成功", users);
    }

    /**
     * 根据 id 查询用户
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        Optional<User> optionalUser = userService.getUserById(id);

        if (optionalUser.isPresent()) {
            return Result.success("查询成功", optionalUser.get());
        }

        return Result.error("用户不存在");
    }

    /**
     * 新增用户
     */
    @PostMapping
    public Result<User> addUser(@RequestBody User user) {
        try {
            return Result.success("新增成功", userService.addUser(user));
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改用户
     */
    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            return Result.success("修改成功", userService.updateUser(id, user));
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return Result.success("删除成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 条件查询用户
     */
    @GetMapping("/search")
    public Result<List<User>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status) {

        List<User> users = userService.searchUsers(username, role, status);

        if (users == null || users.isEmpty()) {
            return Result.success("未查询到相关数据", users);
        }

        return Result.success("查询成功", users);
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        try {
            User user = userService.login(loginRequest.get("username"), loginRequest.get("password"));

            Map<String, Object> result = new HashMap<>();
            result.put("id", user.getId());
            result.put("username", user.getUsername());
            result.put("realName", user.getRealName());
            result.put("role", user.getRole());
            result.put("status", user.getStatus());

            return Result.success("登录成功", result);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}