package com.fzx.drivertrainingmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Driver 类：对应数据库中的 drivers 表
 * 表示“驾驶员信息”
 */
@Entity
@Table(name = "drivers")
@Data
public class Driver {

    /**
     * 主键 id
     * 数据库自动递增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 驾驶员编号
     * 例如：D001、D002
     * 不能为空，且不能重复
     */
    @Column(name = "driver_id", nullable = false, unique = true, length = 20)
    private String driverId;

    /**
     * 驾驶员姓名
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
     */
    @Column(name = "id_card", nullable = false, unique = true, length = 18)
    private String idCard;

    /**
     * 一个驾驶员可以对应多辆车
     * mappedBy = "driver" 表示关系由 Vehicle 里的 driver 字段维护
     *
     * @JsonIgnore 的作用：
     * 防止查询 Driver 时，把它的车辆列表再查出来，
     * 而车辆里又有 Driver，导致 JSON 无限循环
     */
    @OneToMany(mappedBy = "driver")
    @JsonIgnore
    private List<Vehicle> vehicles;
}