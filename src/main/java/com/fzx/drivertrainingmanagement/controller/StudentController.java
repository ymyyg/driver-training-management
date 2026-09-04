package com.fzx.drivertrainingmanagement.controller;

import com.fzx.drivertrainingmanagement.common.Result;
import com.fzx.drivertrainingmanagement.entity.Student;
import com.fzx.drivertrainingmanagement.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * StudentController：
 * 负责处理学员管理相关接口
 */
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * 查询全部学员
     */
    @GetMapping
    public Result<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();

        if (students == null || students.isEmpty()) {
            return Result.success("暂无学员数据", students);
        }

        return Result.success("查询成功", students);
    }

    /**
     * 条件查询学员
     */
    @GetMapping("/search")
    public Result<List<Student>> searchStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String status) {
        List<Student> students = studentService.searchStudents(name, studentId, status);

        if (students == null || students.isEmpty()) {
            return Result.success("未查询到相关数据", students);
        }

        return Result.success("查询成功", students);
    }

    /**
     * 根据 id 查询单个学员
     */
    @GetMapping("/{id}")
    public Result<Student> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);

        if (student != null) {
            return Result.success("查询成功", student);
        } else {
            return Result.error("查询失败：该学员不存在");
        }
    }

    /**
     * 新增学员
     */
    @PostMapping
    public Result<Student> addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    /**
     * 删除学员
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteStudent(@PathVariable Long id) {
        String message = studentService.deleteStudent(id);

        if ("删除成功".equals(message)) {
            return Result.success(message);
        } else {
            return Result.error(message);
        }
    }

    /**
     * 修改学员
     */
    @PutMapping("/{id}")
    public Result<Student> updateStudent(@PathVariable Long id, @RequestBody Student newStudent) {
        Student updatedStudent = studentService.updateStudent(id, newStudent);

        if (updatedStudent != null) {
            return Result.success("修改成功", updatedStudent);
        } else {
            return Result.error("修改失败：该学员不存在");
        }
    }
}