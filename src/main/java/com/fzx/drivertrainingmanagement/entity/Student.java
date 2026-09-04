package com.fzx.drivertrainingmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Student 类：对应数据库中的 students 表
 * 表示“学员信息”
 */
@Entity
@Table(name = "students")
@Data
public class Student {

    /**
     * 主键 id
     * 数据库自动递增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 学员编号
     * 例如：S001、S002
     * 不能为空，不能重复
     */
    @Column(name = "student_id", nullable = false, unique = true, length = 20)
    private String studentId;

    /**
     * 学员姓名
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * 性别
     */
    @Column(nullable = false, length = 10)
    private String gender;

    /**
     * 出生日期
     */
    @Column(name = "birth_date")
    private LocalDate birthDate;

    /**
     * 身份证号
     * 不能为空，不能重复
     */
    @Column(name = "id_card", nullable = false, unique = true, length = 18)
    private String idCard;

    /**
     * 联系电话
     */
    @Column(length = 20)
    private String phone;

    /**
     * 报名日期
     */
    @Column(name = "register_date")
    private LocalDate registerDate;

    /**
     * 学习状态
     * 例如：报名中、培训中、已结业
     */
    @Column(length = 20)
    private String status;
}