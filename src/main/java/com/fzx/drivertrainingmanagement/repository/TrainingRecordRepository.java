package com.fzx.drivertrainingmanagement.repository;

import com.fzx.drivertrainingmanagement.entity.TrainingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * TrainingRecordRepository：
 * 用来操作 training_records 表
 */
public interface TrainingRecordRepository extends JpaRepository<TrainingRecord, Long> {

    /**
     * 判断培训记录编号是否已存在
     */
    boolean existsByRecordId(String recordId);

    /**
     * 按培训记录编号查询
     */
    Optional<TrainingRecord> findByRecordId(String recordId);

    /**
     * 按培训日期查询
     */
    List<TrainingRecord> findByTrainingDate(LocalDate trainingDate);

    /**
     * 按培训内容模糊查询
     */
    List<TrainingRecord> findByTrainingContentContaining(String trainingContent);

    /**
     * 按培训状态查询
     */
    List<TrainingRecord> findByStatus(String status);
}