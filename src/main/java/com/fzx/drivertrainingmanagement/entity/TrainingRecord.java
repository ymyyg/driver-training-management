package com.fzx.drivertrainingmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * TrainingRecord 类：对应数据库中的 training_records 表
 * 表示“培训记录信息”
 */
@Entity
@Table(name = "training_records")
@Data
public class TrainingRecord {

    /**
     * 主键 id
     * 数据库自动递增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 培训记录编号
     * 例如：T001、T002
     * 不能为空，不能重复
     */
    @Column(name = "record_id", nullable = false, unique = true, length = 20)
    private String recordId;

    /**
     * 培训日期
     */
    @Column(name = "training_date")
    private LocalDate trainingDate;

    /**
     * 培训内容
     * 例如：科目二倒车入库、科目三路考训练
     */
    @Column(name = "training_content", length = 100)
    private String trainingContent;

    /**
     * 培训时长（单位：小时）
     */
    @Column(name = "training_hours")
    private Double trainingHours;

    /**
     * 培训状态
     * 例如：已完成、进行中、未开始
     */
    @Column(length = 20)
    private String status;

    /**
     * 多对一关系：
     * 多条培训记录可以属于一个学员
     */
    @ManyToOne
    @JoinColumn(name = "student_id_fk")
    private Student student;

    /**
     * 多对一关系：
     * 多条培训记录可以属于一个教练员
     * 当前这里使用 Driver，业务上先把它理解为教练员
     */
    @ManyToOne
    @JoinColumn(name = "driver_id_fk")
    private Driver driver;

    /**
     * 多对一关系：
     * 多条培训记录可以使用同一辆车
     */
    @ManyToOne
    @JoinColumn(name = "vehicle_id_fk")
    private Vehicle vehicle;
}