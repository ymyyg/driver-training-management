package com.fzx.drivertrainingmanagement.controller;

import com.fzx.drivertrainingmanagement.common.Result;
import com.fzx.drivertrainingmanagement.entity.Driver;
import com.fzx.drivertrainingmanagement.service.DriverService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * DriverController：
 * 负责处理教练员（当前代码里还是 Driver）相关接口
 */
@RestController
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    /**
     * 查询全部教练员
     */
    @GetMapping
    public Result<List<Driver>> getAllDrivers() {
        List<Driver> drivers = driverService.getAllDrivers();

        if (drivers == null || drivers.isEmpty()) {
            return Result.success("暂无教练员数据", drivers);
        }

        return Result.success("查询成功", drivers);
    }

    /**
     * 单条件查询教练员
     */
    @GetMapping("/search")
    public Result<List<Driver>> searchDrivers(
            @RequestParam(required = false) String driverId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
            @RequestParam(required = false) String idCard) {

        List<Driver> drivers = driverService.searchDrivers(driverId, name, gender, birthDate, idCard);

        if (drivers == null || drivers.isEmpty()) {
            return Result.success("未查询到相关数据", drivers);
        }

        return Result.success("查询成功", drivers);
    }

    /**
     * 根据 id 查询单个教练员
     */
    @GetMapping("/{id}")
    public Result<Driver> getDriverById(@PathVariable Long id) {
        Driver driver = driverService.getDriverById(id);

        if (driver != null) {
            return Result.success("查询成功", driver);
        } else {
            return Result.error("查询失败：该教练员不存在");
        }
    }

    /**
     * 新增教练员
     */
    @PostMapping
    public Result<Driver> addDriver(@RequestBody Driver driver) {
        return driverService.addDriver(driver);
    }

    /**
     * 删除教练员
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteDriver(@PathVariable Long id) {
        String message = driverService.deleteDriver(id);

        if ("删除成功".equals(message)) {
            return Result.success(message);
        } else {
            return Result.error(message);
        }
    }

    /**
     * 修改教练员
     */
    @PutMapping("/{id}")
    public Result<Driver> updateDriver(@PathVariable Long id, @RequestBody Driver newDriver) {
        try {
            Driver updatedDriver = driverService.updateDriver(id, newDriver);

            if (updatedDriver != null) {
                return Result.success("修改成功", updatedDriver);
            } else {
                return Result.error("修改失败：该教练员不存在");
            }
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}