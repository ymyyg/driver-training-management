package com.fzx.drivertrainingmanagement.service;

import com.fzx.drivertrainingmanagement.common.Result;
import com.fzx.drivertrainingmanagement.entity.TrainingRecord;
import com.fzx.drivertrainingmanagement.repository.TrainingRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * TrainingRecordService：
 * 负责处理培训记录相关的业务逻辑
 */
@Service
public class TrainingRecordService {

    private final TrainingRecordRepository trainingRecordRepository;

    public TrainingRecordService(TrainingRecordRepository trainingRecordRepository) {
        this.trainingRecordRepository = trainingRecordRepository;
    }

    /**
     * 查询全部培训记录
     */
    public List<TrainingRecord> getAllTrainingRecords() {
        return trainingRecordRepository.findAll();
    }

    /**
     * 根据 id 查询单个培训记录
     */
    public TrainingRecord getTrainingRecordById(Long id) {
        Optional<TrainingRecord> trainingRecordOptional = trainingRecordRepository.findById(id);
        return trainingRecordOptional.orElse(null);
    }

    /**
     * 新增培训记录
     * 如果前端没有传 recordId，就自动生成
     * 同时检查编号是否重复
     */
    public Result<TrainingRecord> addTrainingRecord(TrainingRecord trainingRecord) {
        if (trainingRecord.getRecordId() == null || trainingRecord.getRecordId().isEmpty()) {
            trainingRecord.setRecordId(generateNextRecordId());
        }

        // 检查培训记录编号是否重复
        if (trainingRecordRepository.existsByRecordId(trainingRecord.getRecordId())) {
            return Result.error("新增失败：培训记录编号已存在");
        }

        TrainingRecord savedTrainingRecord = trainingRecordRepository.save(trainingRecord);
        return Result.success("新增成功", savedTrainingRecord);
    }

    /**
     * 根据 id 删除培训记录
     */
    public String deleteTrainingRecord(Long id) {
        if (!trainingRecordRepository.existsById(id)) {
            return "删除失败：该培训记录不存在";
        }

        trainingRecordRepository.deleteById(id);
        return "删除成功";
    }

    /**
     * 根据 id 修改培训记录
     */
    public TrainingRecord updateTrainingRecord(Long id, TrainingRecord newTrainingRecord) {
        Optional<TrainingRecord> trainingRecordOptional = trainingRecordRepository.findById(id);

        if (trainingRecordOptional.isPresent()) {
            TrainingRecord oldTrainingRecord = trainingRecordOptional.get();

            // 检查培训记录编号是否与其他记录重复
            Optional<TrainingRecord> recordByRecordId = trainingRecordRepository.findByRecordId(newTrainingRecord.getRecordId());
            if (recordByRecordId.isPresent() && !recordByRecordId.get().getId().equals(id)) {
                throw new RuntimeException("修改失败：培训记录编号已存在");
            }

            oldTrainingRecord.setRecordId(newTrainingRecord.getRecordId());
            oldTrainingRecord.setTrainingDate(newTrainingRecord.getTrainingDate());
            oldTrainingRecord.setTrainingContent(newTrainingRecord.getTrainingContent());
            oldTrainingRecord.setTrainingHours(newTrainingRecord.getTrainingHours());
            oldTrainingRecord.setStatus(newTrainingRecord.getStatus());
            oldTrainingRecord.setStudent(newTrainingRecord.getStudent());
            oldTrainingRecord.setDriver(newTrainingRecord.getDriver());
            oldTrainingRecord.setVehicle(newTrainingRecord.getVehicle());

            return trainingRecordRepository.save(oldTrainingRecord);
        }

        return null;
    }

    /**
     * 单条件查询培训记录
     * 查询优先级：
     * recordId > trainingDate > trainingContent > status
     */
    public List<TrainingRecord> searchTrainingRecords(String recordId,
                                                      LocalDate trainingDate,
                                                      String trainingContent,
                                                      String status) {
        if (recordId != null && !recordId.isEmpty()) {
            return trainingRecordRepository.findByRecordId(recordId)
                    .map(List::of)
                    .orElse(List.of());
        }

        if (trainingDate != null) {
            return trainingRecordRepository.findByTrainingDate(trainingDate);
        }

        if (trainingContent != null && !trainingContent.isEmpty()) {
            return trainingRecordRepository.findByTrainingContentContaining(trainingContent);
        }

        if (status != null && !status.isEmpty()) {
            return trainingRecordRepository.findByStatus(status);
        }

        return trainingRecordRepository.findAll();
    }

    /**
     * 自动生成下一个培训记录编号
     * 格式：T001、T002、T003...
     */
    private String generateNextRecordId() {
        List<TrainingRecord> trainingRecords = trainingRecordRepository.findAll();

        int maxNumber = 0;

        for (TrainingRecord trainingRecord : trainingRecords) {
            String recordId = trainingRecord.getRecordId();

            if (recordId != null && recordId.startsWith("T")) {
                try {
                    int number = Integer.parseInt(recordId.substring(1));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException e) {
                    // 如果编号格式异常，就跳过
                }
            }
        }

        int nextNumber = maxNumber + 1;

        return String.format("T%03d", nextNumber);
    }
}