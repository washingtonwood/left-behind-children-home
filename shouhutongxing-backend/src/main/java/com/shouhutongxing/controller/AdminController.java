package com.shouhutongxing.controller;

import com.shouhutongxing.dto.Result;
import com.shouhutongxing.entity.Student;
import com.shouhutongxing.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 系统管理控制器
 *
 * @author washingtonwood
 * @since 2025-03-11
 */
@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final StudentRepository studentRepository;
    private final JdbcTemplate jdbcTemplate;

    /**
     * 重新初始化学生数据（清理错误数据）
     */
    @PostMapping("/reinit-students")
    public Result<String> reinitStudents() {
        log.info("开始重新初始化学生数据...");

        try {
            // 使用原生 SQL 清空表（绕过枚举映射问题）
            jdbcTemplate.execute("TRUNCATE TABLE student");
            jdbcTemplate.execute("TRUNCATE TABLE material_demand");
            log.info("已清空 student 和 material_demand 表");

            // 插入新的测试数据
            List<Student> students = List.of(
                createStudent("2025001", "小明", "男", 5, "5年1班", 92.00,
                        "父母在外务工，跟随年迈的祖父母生活", "急需学习用品和冬季衣物"),
                createStudent("2025002", "小红", "女", 4, "4年2班", 88.00,
                        "单亲家庭，母亲患病丧失劳动能力", "急需课外读物和文具"),
                createStudent("2025003", "小强", "男", 6, "6年1班", 85.00,
                        "家里有三个孩子上学，经济压力大", "需要辅导资料和体育用品"),
                createStudent("2025004", "小丽", "女", 3, "3年1班", 80.00,
                        "留守儿童，父母常年在外打工", "需要绘画用品和益智玩具"),
                createStudent("2025005", "小华", "男", 5, "5年2班", 78.00,
                        "家庭务农为主要收入，收入不稳定", "需要课外读物和书包"),
                createStudent("2025006", "小芳", "女", 4, "4年1班", 75.00,
                        "家庭人口多，负担重", "需要文具和美术用品"),
                createStudent("2025007", "小军", "男", 6, "6年2班", 72.00,
                        "父亲因病丧失劳动能力", "急需科普读物和学习用品"),
                createStudent("2025008", "小梅", "女", 3, "3年2班", 70.00,
                        "偏远山区，交通不便", "需要保暖衣物和文具"),
                createStudent("2025009", "小伟", "男", 5, "5年3班", 68.00,
                        "经济来源单一", "需要体育用品和课外读物"),
                createStudent("2025010", "小娜", "女", 4, "4年3班", 65.00,
                        "家庭经济困难，缺乏基本学习用品", "急需全套文具和书籍"),
                createStudent("2025011", "小东", "男", 6, "6年3班", 62.00,
                        "父母收入低，供养多个子女", "需要辅导资料和工具书"),
                createStudent("2025012", "小静", "女", 3, "3年3班", 60.00,
                        "一般困难家庭", "需要美术用品和绘本")
            );

            studentRepository.saveAll(students);
            log.info("成功插入 {} 条学生记录", students.size());

            return Result.success("数据重新初始化成功！", "共插入 " + students.size() + " 条学生记录");
        } catch (Exception e) {
            log.error("重新初始化失败", e);
            return Result.error("初始化失败：" + e.getMessage());
        }
    }

    private Student createStudent(String sn, String name, String gender, Integer year,
                                   String clazz, Double weight, String familyCondition, String urgentNeed) {
        Student student = new Student();
        student.setSn(sn);
        student.setName(name);
        student.setGender(gender);
        student.setYear(year);
        student.setClazz(clazz);
        student.setRecommendationWeight(new BigDecimal(weight));
        student.setPhoto("/uploads/students/photo" + Math.abs(name.hashCode()) % 6 + 1 + ".jpg");
        student.setFamilyCondition(familyCondition);
        student.setUrgentNeed(urgentNeed);
        return student;
    }

    /**
     * 获取数据库状态
     */
    @GetMapping("/db-status")
    public Result<String> getDbStatus() {
        long count = studentRepository.count();
        return Result.success("数据库状态", "student 表共有 " + count + " 条记录");
    }
}
