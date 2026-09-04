package com.fzx.drivertrainingmanagement.service;

import com.fzx.drivertrainingmanagement.common.Result;
import com.fzx.drivertrainingmanagement.entity.Driver;
import com.fzx.drivertrainingmanagement.repository.DriverRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * DriverService：
 * 负责处理教练员（当前代码中仍命名为 Driver）相关业务逻辑
 */
@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    /**
     * 查询全部教练员
     */
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    /**
     * 根据 id 查询单个教练员
     */
    public Driver getDriverById(Long id) {
        Optional<Driver> driverOptional = driverRepository.findById(id);
        return driverOptional.orElse(null);
    }

    /**
     * 新增教练员
     * 如果前端没有传 driverId，就自动生成
     * 同时检查编号和身份证号是否重复
     */
    public Result<Driver> addDriver(Driver driver) {
        if (driver.getDriverId() == null || driver.getDriverId().isEmpty()) {
            driver.setDriverId(generateNextDriverId());
        }

        // 检查教练员编号是否重复
        if (driverRepository.existsByDriverId(driver.getDriverId())) {
            return Result.error("新增失败：教练员编号已存在");
        }

        // 检查身份证号是否重复
        if (driverRepository.existsByIdCard(driver.getIdCard())) {
            return Result.error("新增失败：身份证号已存在");
        }

        Driver savedDriver = driverRepository.save(driver);
        return Result.success("新增成功", savedDriver);
    }

    /**
     * 根据 id 删除教练员
     */
    public String deleteDriver(Long id) {
        if (!driverRepository.existsById(id)) {
            return "删除失败：该教练员不存在";
        }

        try {
            driverRepository.deleteById(id);
            return "删除成功";
        } catch (DataIntegrityViolationException e) {
            return "删除失败：该教练员已关联车辆或培训记录，无法直接删除";
        }
    }

    /**
     * 根据 id 修改教练员
     */
    public Driver updateDriver(Long id, Driver newDriver) {
        Optional<Driver> driverOptional = driverRepository.findById(id);

        if (driverOptional.isPresent()) {
            Driver oldDriver = driverOptional.get();

            // 检查教练员编号是否与其他记录重复
            Optional<Driver> driverByDriverId = driverRepository.findByDriverId(newDriver.getDriverId());
            if (driverByDriverId.isPresent() && !driverByDriverId.get().getId().equals(id)) {
                throw new RuntimeException("修改失败：教练员编号已存在");
            }

            // 检查身份证号是否与其他记录重复
            Optional<Driver> driverByIdCard = driverRepository.findByIdCard(newDriver.getIdCard());
            if (driverByIdCard.isPresent() && !driverByIdCard.get().getId().equals(id)) {
                throw new RuntimeException("修改失败：身份证号已存在");
            }

            oldDriver.setDriverId(newDriver.getDriverId());
            oldDriver.setName(newDriver.getName());
            oldDriver.setGender(newDriver.getGender());
            oldDriver.setBirthDate(newDriver.getBirthDate());
            oldDriver.setIdCard(newDriver.getIdCard());

            return driverRepository.save(oldDriver);
        }

        return null;
    }

    /**
     * 单条件查询教练员
     * 查询优先级：
     * driverId > name > gender > birthDate > idCard
     */
    public List<Driver> searchDrivers(String driverId, String name, String gender, LocalDate birthDate, String idCard) {
        if (driverId != null && !driverId.isEmpty()) {
            return driverRepository.findByDriverId(driverId)
                    .map(List::of)
                    .orElse(List.of());
        }

        if (name != null && !name.isEmpty()) {
            return driverRepository.findByNameContaining(name);
        }

        if (gender != null && !gender.isEmpty()) {
            return driverRepository.findByGender(gender);
        }

        if (birthDate != null) {
            return driverRepository.findByBirthDate(birthDate);
        }

        if (idCard != null && !idCard.isEmpty()) {
            return driverRepository.findByIdCard(idCard)
                    .map(List::of)
                    .orElse(List.of());
        }

        return driverRepository.findAll();
    }

    /**
     * 自动生成下一个教练员编号
     * 格式：D001、D002、D003...
     */
    private String generateNextDriverId() {
        List<Driver> drivers = driverRepository.findAll();

        int maxNumber = 0;

        for (Driver driver : drivers) {
            String driverId = driver.getDriverId();

            if (driverId != null && driverId.startsWith("D")) {
                try {
                    int number = Integer.parseInt(driverId.substring(1));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException e) {
                    // 格式异常就跳过
                }
            }
        }

        int nextNumber = maxNumber + 1;

        return String.format("D%03d", nextNumber);
    }
}