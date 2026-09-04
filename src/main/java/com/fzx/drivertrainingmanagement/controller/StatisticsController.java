package com.fzx.drivertrainingmanagement.controller;

import com.fzx.drivertrainingmanagement.common.Result;
import com.fzx.drivertrainingmanagement.entity.Student;
import com.fzx.drivertrainingmanagement.entity.TrainingRecord;
import com.fzx.drivertrainingmanagement.entity.Vehicle;
import com.fzx.drivertrainingmanagement.repository.DriverRepository;
import com.fzx.drivertrainingmanagement.repository.StudentRepository;
import com.fzx.drivertrainingmanagement.repository.TrainingRecordRepository;
import com.fzx.drivertrainingmanagement.repository.UserRepository;
import com.fzx.drivertrainingmanagement.repository.VehicleRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StatisticsController：
 * 负责处理统计相关接口
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private final StudentRepository studentRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final TrainingRecordRepository trainingRecordRepository;
    private final UserRepository userRepository;

    public StatisticsController(StudentRepository studentRepository,
                                DriverRepository driverRepository,
                                VehicleRepository vehicleRepository,
                                TrainingRecordRepository trainingRecordRepository,
                                UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.trainingRecordRepository = trainingRecordRepository;
        this.userRepository = userRepository;
    }

    /**
     * 系统总览统计
     */
    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverviewStatistics() {
        Map<String, Object> data = new HashMap<>();

        // 总数统计
        data.put("studentCount", studentRepository.count());
        data.put("driverCount", driverRepository.count());
        data.put("vehicleCount", vehicleRepository.count());
        data.put("trainingRecordCount", trainingRecordRepository.count());
        data.put("userCount", userRepository.count());

        // 学员状态统计
        List<Student> students = studentRepository.findAll();
        Map<String, Long> studentStatusStatistics = new HashMap<>();
        for (Student student : students) {
            String status = student.getStatus();
            if (status == null || status.isEmpty()) {
                status = "未设置";
            }
            studentStatusStatistics.put(status, studentStatusStatistics.getOrDefault(status, 0L) + 1);
        }
        data.put("studentStatusStatistics", studentStatusStatistics);

        // 车辆状态统计
        List<Vehicle> vehicles = vehicleRepository.findAll();
        Map<String, Long> vehicleStatusStatistics = new HashMap<>();
        for (Vehicle vehicle : vehicles) {
            String status = vehicle.getStatus();
            if (status == null || status.isEmpty()) {
                status = "未设置";
            }
            vehicleStatusStatistics.put(status, vehicleStatusStatistics.getOrDefault(status, 0L) + 1);
        }
        data.put("vehicleStatusStatistics", vehicleStatusStatistics);

        // 培训记录状态统计
        List<TrainingRecord> trainingRecords = trainingRecordRepository.findAll();
        Map<String, Long> trainingRecordStatusStatistics = new HashMap<>();
        for (TrainingRecord trainingRecord : trainingRecords) {
            String status = trainingRecord.getStatus();
            if (status == null || status.isEmpty()) {
                status = "未设置";
            }
            trainingRecordStatusStatistics.put(status, trainingRecordStatusStatistics.getOrDefault(status, 0L) + 1);
        }
        data.put("trainingRecordStatusStatistics", trainingRecordStatusStatistics);

        return Result.success("查询成功", data);
    }
}