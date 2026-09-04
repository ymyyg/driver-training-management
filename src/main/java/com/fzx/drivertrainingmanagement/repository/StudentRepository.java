package com.fzx.drivertrainingmanagement.repository;

import com.fzx.drivertrainingmanagement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * StudentRepository：
 * 用来操作 students 表
 */
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * 判断学员编号是否已存在
     */
    boolean existsByStudentId(String studentId);

    /**
     * 判断身份证号是否已存在
     */
    boolean existsByIdCard(String idCard);

    /**
     * 按姓名模糊查询
     */
    List<Student> findByNameContaining(String name);

    /**
     * 按学员编号查询
     */
    Optional<Student> findByStudentId(String studentId);

    /**
     * 按状态查询
     */
    List<Student> findByStatus(String status);
}