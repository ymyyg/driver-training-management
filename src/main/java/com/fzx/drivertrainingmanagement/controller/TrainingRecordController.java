package com.fzx.drivertrainingmanagement.controller;

import com.fzx.drivertrainingmanagement.common.Result;
import com.fzx.drivertrainingmanagement.entity.TrainingRecord;
import com.fzx.drivertrainingmanagement.service.TrainingRecordService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * TrainingRecordController：
 * 负责处理培训记录相关接口
 */
@RestController
@RequestMapping("/training-records")
public class TrainingRecordController {

    private final TrainingRecordService trainingRecordService;

    public TrainingRecordController(TrainingRecordService trainingRecordService) {
        this.trainingRecordService = trainingRecordService;
    }

    /**
     * 查询全部培训记录
     */
    @GetMapping
    public Result<List<TrainingRecord>> getAllTrainingRecords() {
        List<TrainingRecord> trainingRecords = trainingRecordService.getAllTrainingRecords();

        if (trainingRecords == null || trainingRecords.isEmpty()) {
            return Result.success("暂无培训记录数据", trainingRecords);
        }

        return Result.success("查询成功", trainingRecords);
    }

    /**
     * 单条件查询培训记录
     */
    @GetMapping("/search")
    public Result<List<TrainingRecord>> searchTrainingRecords(
            @RequestParam(required = false) String recordId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate trainingDate,
            @RequestParam(required = false) String trainingContent,
            @RequestParam(required = false) String status) {

        List<TrainingRecord> trainingRecords = trainingRecordService.searchTrainingRecords(
                recordId, trainingDate, trainingContent, status
        );

        if (trainingRecords == null || trainingRecords.isEmpty()) {
            return Result.success("未查询到相关数据", trainingRecords);
        }

        return Result.success("查询成功", trainingRecords);
    }

    /**
     * 根据 id 查询单个培训记录
     */
    @GetMapping("/{id}")
    public Result<TrainingRecord> getTrainingRecordById(@PathVariable Long id) {
        TrainingRecord trainingRecord = trainingRecordService.getTrainingRecordById(id);

        if (trainingRecord != null) {
            return Result.success("查询成功", trainingRecord);
        } else {
            return Result.error("查询失败：该培训记录不存在");
        }
    }

    /**
     * 新增培训记录
     */
    @PostMapping
    public Result<TrainingRecord> addTrainingRecord(@RequestBody TrainingRecord trainingRecord) {
        return trainingRecordService.addTrainingRecord(trainingRecord);
    }

    /**
     * 删除培训记录
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteTrainingRecord(@PathVariable Long id) {
        String message = trainingRecordService.deleteTrainingRecord(id);

        if ("删除成功".equals(message)) {
            return Result.success(message);
        } else {
            return Result.error(message);
        }
    }

    /**
     * 修改培训记录
     */
    @PutMapping("/{id}")
    public Result<TrainingRecord> updateTrainingRecord(@PathVariable Long id,
                                                       @RequestBody TrainingRecord newTrainingRecord) {
        try {
            TrainingRecord updatedTrainingRecord = trainingRecordService.updateTrainingRecord(id, newTrainingRecord);

            if (updatedTrainingRecord != null) {
                return Result.success("修改成功", updatedTrainingRecord);
            } else {
                return Result.error("修改失败：该培训记录不存在");
            }
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}