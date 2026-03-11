package com.shouhutongxing.controller;

import com.shouhutongxing.dto.Result;
import com.shouhutongxing.entity.Student;
import com.shouhutongxing.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 学生推荐控制器
 *
 * @author washingtonwood
 * @since 2025-03-11
 */
@Slf4j
@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    /**
     * 获取推荐学生列表（核心接口）
     *
     * @param category 物资分类（可选）
     * @param familyIncomeLevel 家庭收入水平（可选）
     * @param minYear 最小年级（可选）
     * @param maxYear 最大年级（可选）
     * @param gender 性别（可选）
     * @param limit 返回数量限制（默认20）
     * @return 推荐学生列表
     */
    @GetMapping("/recommendations")
    public Result<List<Map<String, Object>>> getRecommendations(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String familyIncomeLevel,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false, defaultValue = "20") Integer limit) {

        log.info("获取推荐学生，category={}, familyIncomeLevel={}, yearRange=[{},{}], gender={}, limit={}",
                category, familyIncomeLevel, minYear, maxYear, gender, limit);

        List<Map<String, Object>> recommendations = studentService.getRecommendedStudents(
                category, familyIncomeLevel, minYear, maxYear, gender, limit);

        return Result.success("获取推荐列表成功", recommendations);
    }

    /**
     * 获取学生详情
     *
     * @param id 学生ID
     * @return 学生详情
     */
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getStudentDetail(@PathVariable Integer id) {
        log.info("获取学生详情，id={}", id);
        Map<String, Object> detail = studentService.getStudentDetail(id);
        return Result.success("获取学生详情成功", detail);
    }

    /**
     * 获取统计数据（用于筛选器）
     *
     * @return 统计数据
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        log.info("获取学生统计数据");
        Map<String, Object> stats = studentService.getStatistics();
        return Result.success("获取统计数据成功", stats);
    }

    /**
     * 搜索学生
     *
     * @param keyword 搜索关键词
     * @return 搜索结果
     */
    @GetMapping("/search")
    public Result<List<Student>> searchStudents(@RequestParam String keyword) {
        log.info("搜索学生，keyword={}", keyword);
        List<Student> students = studentService.searchStudents(keyword);
        return Result.success("搜索成功", students);
    }

    /**
     * 获取高优先级推荐学生
     *
     * @param limit 返回数量限制（默认10）
     * @return 高优先级学生列表
     */
    @GetMapping("/high-priority")
    public Result<List<Map<String, Object>>> getHighPriorityStudents(
            @RequestParam(required = false, defaultValue = "10") Integer limit) {

        log.info("获取高优先级学生，limit={}", limit);

        // 复用推荐方法，设置最低权重为85
        List<Map<String, Object>> students = studentService.getRecommendedStudents(
                null, null, null, null, null, limit);

        // 过滤出高优先级学生（权重>=85）
        List<Map<String, Object>> highPriority = students.stream()
                .filter(s -> {
                    Object weightObj = s.get("recommendationWeight");
                    if (weightObj instanceof Number) {
                        return ((Number) weightObj).doubleValue() >= 85;
                    }
                    return false;
                })
                .limit(limit)
                .toList();

        return Result.success("获取高优先级学生成功", highPriority);
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("学生推荐模块运行正常", "OK");
    }
}
