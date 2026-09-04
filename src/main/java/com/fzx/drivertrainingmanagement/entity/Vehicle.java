package com.fzx.drivertrainingmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Vehicle 类：对应数据库中的 vehicles 表
 * 表示“车辆信息”
 */
@Entity
@Table(name = "vehicles")
@Data
public class Vehicle {

    /**
     * 主键 id
     * 数据库自动递增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 车辆编号
     * 例如：V001、V002
     * 不能为空，不能重复
     */
    @Column(name = "vehicle_id", nullable = false, unique = true, length = 20)
    private String vehicleId;

    /**
     * 车牌号
     * 不能为空，不能重复
     */
    @Column(name = "plate_number", nullable = false, unique = true, length = 20)
    private String plateNumber;

    /**
     * 品牌
     * 例如：丰田、本田、大众
     */
    @Column(nullable = false, length = 50)
    private String brand;

    /**
     * 型号
     * 例如：卡罗拉、思域
     */
    @Column(length = 50)
    private String model;

    /**
     * 车辆状态
     * 例如：可用、使用中、维修中
     */
    @Column(length = 20)
    private String status;

    /**
     * 多对一关系：
     * 多辆车可以属于一个驾驶员
     *
     * @JoinColumn(name = "driver_id_fk") 表示：
     * vehicles 表中会新增一个外键字段 driver_id_fk
     * 用来关联 drivers 表的主键 id
     */
    @ManyToOne
    @JoinColumn(name = "driver_id_fk")
    private Driver driver;
}