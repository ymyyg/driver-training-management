package com.fzx.drivertrainingmanagement.repository;

import com.fzx.drivertrainingmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository：
 * 用来操作 users 表
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查询
     */
    Optional<User> findByUsername(String username);

    /**
     * 按用户名模糊查询
     */
    List<User> findByUsernameContaining(String username);

    /**
     * 按角色查询
     */
    List<User> findByRole(String role);

    /**
     * 按状态查询
     */
    List<User> findByStatus(String status);
}