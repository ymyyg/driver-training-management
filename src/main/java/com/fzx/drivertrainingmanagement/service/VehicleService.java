package com.fzx.drivertrainingmanagement.service;

import com.fzx.drivertrainingmanagement.common.Result;
import com.fzx.drivertrainingmanagement.entity.Vehicle;
import com.fzx.drivertrainingmanagement.repository.VehicleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * VehicleService：
 * 负责处理车辆相关的业务逻辑
 */
@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * 查询全部车辆
     */
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    /**
     * 根据 id 查询单个车辆
     */
    public Vehicle getVehicleById(Long id) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(id);
        return vehicleOptional.orElse(null);
    }

    /**
     * 新增车辆
     * 如果前端没有传 vehicleId，就自动生成
     * 同时检查编号和车牌号是否重复
     */
    public Result<Vehicle> addVehicle(Vehicle vehicle) {
        if (vehicle.getVehicleId() == null || vehicle.getVehicleId().isEmpty()) {
            vehicle.setVehicleId(generateNextVehicleId());
        }

        // 检查车辆编号是否重复
        if (vehicleRepository.existsByVehicleId(vehicle.getVehicleId())) {
            return Result.error("新增失败：车辆编号已存在");
        }

        // 检查车牌号是否重复
        if (vehicleRepository.existsByPlateNumber(vehicle.getPlateNumber())) {
            return Result.error("新增失败：车牌号已存在");
        }

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return Result.success("新增成功", savedVehicle);
    }

    /**
     * 根据 id 删除车辆
     */
    public String deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            return "删除失败：该车辆不存在";
        }

        try {
            vehicleRepository.deleteById(id);
            return "删除成功";
        } catch (DataIntegrityViolationException e) {
            return "删除失败：该车辆已被培训记录使用，无法直接删除";
        }
    }

    /**
     * 根据 id 修改车辆
     */
    public Vehicle updateVehicle(Long id, Vehicle newVehicle) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(id);

        if (vehicleOptional.isPresent()) {
            Vehicle oldVehicle = vehicleOptional.get();

            // 检查车辆编号是否与其他记录重复
            Optional<Vehicle> vehicleByVehicleId = vehicleRepository.findByVehicleId(newVehicle.getVehicleId());
            if (vehicleByVehicleId.isPresent() && !vehicleByVehicleId.get().getId().equals(id)) {
                throw new RuntimeException("修改失败：车辆编号已存在");
            }

            // 检查车牌号是否与其他记录重复
            Optional<Vehicle> vehicleByPlateNumber = vehicleRepository.findByPlateNumber(newVehicle.getPlateNumber());
            if (vehicleByPlateNumber.isPresent() && !vehicleByPlateNumber.get().getId().equals(id)) {
                throw new RuntimeException("修改失败：车牌号已存在");
            }

            oldVehicle.setVehicleId(newVehicle.getVehicleId());
            oldVehicle.setPlateNumber(newVehicle.getPlateNumber());
            oldVehicle.setBrand(newVehicle.getBrand());
            oldVehicle.setModel(newVehicle.getModel());
            oldVehicle.setStatus(newVehicle.getStatus());
            oldVehicle.setDriver(newVehicle.getDriver());

            return vehicleRepository.save(oldVehicle);
        }

        return null;
    }

    /**
     * 单条件查询车辆
     * 查询优先级：
     * vehicleId > plateNumber > brand > model > status
     */
    public List<Vehicle> searchVehicles(String vehicleId, String plateNumber, String brand, String model, String status) {
        if (vehicleId != null && !vehicleId.isEmpty()) {
            return vehicleRepository.findByVehicleId(vehicleId)
                    .map(List::of)
                    .orElse(List.of());
        }

        if (plateNumber != null && !plateNumber.isEmpty()) {
            return vehicleRepository.findByPlateNumber(plateNumber)
                    .map(List::of)
                    .orElse(List.of());
        }

        if (brand != null && !brand.isEmpty()) {
            return vehicleRepository.findByBrandContaining(brand);
        }

        if (model != null && !model.isEmpty()) {
            return vehicleRepository.findByModelContaining(model);
        }

        if (status != null && !status.isEmpty()) {
            return vehicleRepository.findByStatus(status);
        }

        return vehicleRepository.findAll();
    }

    /**
     * 自动生成下一个车辆编号
     * 格式：V001、V002、V003...
     */
    private String generateNextVehicleId() {
        List<Vehicle> vehicles = vehicleRepository.findAll();

        int maxNumber = 0;

        for (Vehicle vehicle : vehicles) {
            String vehicleId = vehicle.getVehicleId();

            if (vehicleId != null && vehicleId.startsWith("V")) {
                try {
                    int number = Integer.parseInt(vehicleId.substring(1));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException e) {
                    // 格式异常就跳过
                }
            }
        }

        int nextNumber = maxNumber + 1;

        return String.format("V%03d", nextNumber);
    }
}