package com.fzx.drivertrainingmanagement.service;

import com.fzx.drivertrainingmanagement.common.Result;
import com.fzx.drivertrainingmanagement.entity.Student;
import com.fzx.drivertrainingmanagement.repository.StudentRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * StudentService：
 * 负责处理学员相关的业务逻辑
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * 查询全部学员
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * 根据 id 查询单个学员
     */
    public Student getStudentById(Long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        return studentOptional.orElse(null);
    }

    /**
     * 新增学员
     * 如果前端没有传 studentId，就自动生成
     * 同时检查编号和身份证号是否重复
     */
    public Result<Student> addStudent(Student student) {
        // 如果前端没有传 studentId，就自动生成
        if (student.getStudentId() == null || student.getStudentId().isEmpty()) {
            student.setStudentId(generateNextStudentId());
        }

        // 检查学员编号是否重复
        if (studentRepository.existsByStudentId(student.getStudentId())) {
            return Result.error("新增失败：学员编号已存在");
        }

        // 检查身份证号是否重复
        if (studentRepository.existsByIdCard(student.getIdCard())) {
            return Result.error("新增失败：身份证号已存在");
        }

        Student savedStudent = studentRepository.save(student);
        return Result.success("新增成功", savedStudent);
    }

    /**
     * 根据 id 删除学员
     */
    public String deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            return "删除失败：该学员不存在";
        }

        try {
            studentRepository.deleteById(id);
            return "删除成功";
        } catch (DataIntegrityViolationException e) {
            return "删除失败：该学员已有培训记录，无法直接删除";
        }
    }

    /**
     * 根据 id 修改学员
     */
    public Student updateStudent(Long id, Student newStudent) {
        Optional<Student> studentOptional = studentRepository.findById(id);

        if (studentOptional.isPresent()) {
            Student oldStudent = studentOptional.get();

            oldStudent.setStudentId(newStudent.getStudentId());
            oldStudent.setName(newStudent.getName());
            oldStudent.setGender(newStudent.getGender());
            oldStudent.setBirthDate(newStudent.getBirthDate());
            oldStudent.setIdCard(newStudent.getIdCard());
            oldStudent.setPhone(newStudent.getPhone());
            oldStudent.setRegisterDate(newStudent.getRegisterDate());
            oldStudent.setStatus(newStudent.getStatus());

            return studentRepository.save(oldStudent);
        }

        return null;
    }

    /**
     * 条件查询学员
     */
    public List<Student> searchStudents(String name, String studentId, String status) {
        if (name != null && !name.isEmpty()) {
            return studentRepository.findByNameContaining(name);
        }

        if (studentId != null && !studentId.isEmpty()) {
            return studentRepository.findByStudentId(studentId)
                    .map(List::of)
                    .orElse(List.of());
        }

        if (status != null && !status.isEmpty()) {
            return studentRepository.findByStatus(status);
        }

        return studentRepository.findAll();
    }

    /**
     * 自动生成下一个学员编号
     * 格式：S001、S002、S003...
     */
    private String generateNextStudentId() {
        List<Student> students = studentRepository.findAll();

        int maxNumber = 0;

        for (Student student : students) {
            String studentId = student.getStudentId();

            if (studentId != null && studentId.startsWith("S")) {
                try {
                    int number = Integer.parseInt(studentId.substring(1));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException e) {
                    // 格式不对就跳过
                }
            }
        }

        int nextNumber = maxNumber + 1;

        return String.format("S%03d", nextNumber);
    }
}