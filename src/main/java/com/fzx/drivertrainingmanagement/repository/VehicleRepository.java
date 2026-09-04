package com.fzx.drivertrainingmanagement.repository;

import com.fzx.drivertrainingmanagement.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * VehicleRepository：
 * 用来操作 vehicles 表
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    /**
     * 判断车辆编号是否已存在
     */
    boolean existsByVehicleId(String vehicleId);

    /**
     * 判断车牌号是否已存在
     */
    boolean existsByPlateNumber(String plateNumber);

    /**
     * 按车辆编号查询
     */
    Optional<Vehicle> findByVehicleId(String vehicleId);

    /**
     * 按车牌号查询
     */
    Optional<Vehicle> findByPlateNumber(String plateNumber);

    /**
     * 按品牌模糊查询
     */
    List<Vehicle> findByBrandContaining(String brand);

    /**
     * 按型号模糊查询
     */
    List<Vehicle> findByModelContaining(String model);

    /**
     * 按状态查询
     */
    List<Vehicle> findByStatus(String status);
}