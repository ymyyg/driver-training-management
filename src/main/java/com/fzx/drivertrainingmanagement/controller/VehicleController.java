package com.fzx.drivertrainingmanagement.controller;

import com.fzx.drivertrainingmanagement.common.Result;
import com.fzx.drivertrainingmanagement.entity.Vehicle;
import com.fzx.drivertrainingmanagement.service.VehicleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * VehicleController：
 * 负责处理车辆管理相关接口
 */
@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    /**
     * 查询全部车辆
     */
    @GetMapping
    public Result<List<Vehicle>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        if (vehicles == null || vehicles.isEmpty()) {
            return Result.success("暂无车辆数据", vehicles);
        }

        return Result.success("查询成功", vehicles);
    }

    /**
     * 单条件查询车辆
     */
    @GetMapping("/search")
    public Result<List<Vehicle>> searchVehicles(
            @RequestParam(required = false) String vehicleId,
            @RequestParam(required = false) String plateNumber,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String status) {

        List<Vehicle> vehicles = vehicleService.searchVehicles(vehicleId, plateNumber, brand, model, status);

        if (vehicles == null || vehicles.isEmpty()) {
            return Result.success("未查询到相关数据", vehicles);
        }

        return Result.success("查询成功", vehicles);
    }

    /**
     * 根据 id 查询单个车辆
     */
    @GetMapping("/{id}")
    public Result<Vehicle> getVehicleById(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.getVehicleById(id);

        if (vehicle != null) {
            return Result.success("查询成功", vehicle);
        } else {
            return Result.error("查询失败：该车辆不存在");
        }
    }

    /**
     * 新增车辆
     */
    @PostMapping
    public Result<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {
        return vehicleService.addVehicle(vehicle);
    }

    /**
     * 删除车辆
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteVehicle(@PathVariable Long id) {
        String message = vehicleService.deleteVehicle(id);

        if ("删除成功".equals(message)) {
            return Result.success(message);
        } else {
            return Result.error(message);
        }
    }

    /**
     * 修改车辆
     */
    @PutMapping("/{id}")
    public Result<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle newVehicle) {
        try {
            Vehicle updatedVehicle = vehicleService.updateVehicle(id, newVehicle);

            if (updatedVehicle != null) {
                return Result.success("修改成功", updatedVehicle);
            } else {
                return Result.error("修改失败：该车辆不存在");
            }
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}