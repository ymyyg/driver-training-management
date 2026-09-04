package com.fzx.drivertrainingmanagement.repository;

import com.fzx.drivertrainingmanagement.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * DriverRepository：
 * 用来操作 drivers 表
 */
public interface DriverRepository extends JpaRepository<Driver, Long> {

    /**
     * 判断教练员编号是否已存在
     */
    boolean existsByDriverId(String driverId);

    /**
     * 判断身份证号是否已存在
     */
    boolean existsByIdCard(String idCard);

    /**
     * 按姓名模糊查询
     */
    List<Driver> findByNameContaining(String name);

    /**
     * 按教练员编号查询
     */
    Optional<Driver> findByDriverId(String driverId);

    /**
     * 按性别查询
     */
    List<Driver> findByGender(String gender);

    /**
     * 按出生日期查询
     */
    List<Driver> findByBirthDate(LocalDate birthDate);

    /**
     * 按身份证号查询
     */
    Optional<Driver> findByIdCard(String idCard);
}