package com.shouhutongxing.service;

import com.shouhutongxing.entity.Student;
import com.shouhutongxing.entity.MaterialDemand;
import com.shouhutongxing.repository.StudentRepository;
import com.shouhutongxing.repository.MaterialDemandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 学生推荐服务
 *
 * @author washingtonwood
 * @since 2025-03-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final MaterialDemandRepository demandRepository;

    /**
     * 获取推荐学生列表（核心推荐算法）
     *
     * @param category 物资分类（可选）
     * @param familyIncomeLevel 家庭收入水平（可选）
     * @param minYear 最小年级（可选）
     * @param maxYear 最大年级（可选）
     * @param gender 性别（可选）
     * @param limit 返回数量限制
     * @return 推荐学生列表（包含推荐理由）
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getRecommendedStudents(
            String category,
            String familyIncomeLevel,
            Integer minYear,
            Integer maxYear,
            String gender,
            Integer limit) {

        log.info("获取推荐学生，category={}, familyIncomeLevel={}, yearRange=[{},{}], gender={}",
                category, familyIncomeLevel, minYear, maxYear, gender);

        // 1. 基础查询：获取所有学生并按推荐权重排序
        List<Student> students = studentRepository.findAll().stream()
                .sorted((s1, s2) -> s2.getRecommendationWeight().compareTo(s1.getRecommendationWeight()))
                .collect(Collectors.toList());

        // 2. 应用筛选条件
        List<Student> filtered = students.stream()
                .filter(s -> familyIncomeLevel == null ||
                        (s.getFamilyIncomeLevel() != null &&
                         s.getFamilyIncomeLevel().name().equals(familyIncomeLevel)))
                .filter(s -> minYear == null || s.getYear() >= minYear)
                .filter(s -> maxYear == null || s.getYear() <= maxYear)
                .filter(s -> gender == null || gender.equals(s.getGender()))
                .limit(limit != null ? limit : 20)
                .collect(Collectors.toList());

        // 3. 如果指定了物资分类，需要匹配物资需求
        if (category != null && !category.isEmpty()) {
            // 获取有该类物资需求的学生ID
            List<Long> studentIdsWithDemand = demandRepository.findAll().stream()
                    .filter(d -> d.getStatus() == MaterialDemand.DemandStatus.ACTIVE)
                    .filter(d -> category.equals(d.getCategory()))
                    .filter(d -> !d.isFullyFulfilled())
                    .map(MaterialDemand::getChildId)
                    .distinct()
                    .collect(Collectors.toList());

            // 优先显示有需求的学生
            List<Student> priorityStudents = filtered.stream()
                    .filter(s -> studentIdsWithDemand.contains(s.getId().longValue()))
                    .collect(Collectors.toList());

            List<Student> otherStudents = filtered.stream()
                    .filter(s -> !studentIdsWithDemand.contains(s.getId().longValue()))
                    .collect(Collectors.toList());

            // 合并列表（有需求的在前）
            List<Student> combined = new ArrayList<>();
            combined.addAll(priorityStudents);
            combined.addAll(otherStudents);

            filtered = combined;
        }

        // 4. 构建推荐结果（包含推荐理由）
        List<Map<String, Object>> result = new ArrayList<>();
        for (Student student : filtered) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", student.getId());
            item.put("name", student.getName());
            item.put("gender", student.getGender());
            item.put("year", student.getYear());
            item.put("clazz", student.getClazz());
            item.put("photo", student.getPhoto());
            item.put("recommendationWeight", student.getRecommendationWeight());
            item.put("familyIncomeLevel", student.getFamilyIncomeLevel());
            item.put("familyCondition", student.getFamilyCondition());
            item.put("urgentNeed", student.getUrgentNeed());
            item.put("recommendationLevel", student.getRecommendationLevel());
            item.put("recommendationReason", generateRecommendationReason(student, category));
            item.put("demands", getStudentDemands(student.getId().longValue()));

            result.add(item);
        }

        log.info("返回 {} 个推荐学生", result.size());
        return result;
    }

    /**
     * 获取学生详情（包含物资需求）
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getStudentDetail(Integer id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("学生不存在：" + id));

        Map<String, Object> result = new HashMap<>();
        result.put("id", student.getId());
        result.put("name", student.getName());
        result.put("gender", student.getGender());
        result.put("year", student.getYear());
        result.put("clazz", student.getClazz());
        result.put("photo", student.getPhoto());
        result.put("recommendationWeight", student.getRecommendationWeight());
        result.put("familyIncomeLevel", student.getFamilyIncomeLevel());
        result.put("familyCondition", student.getFamilyCondition());
        result.put("urgentNeed", student.getUrgentNeed());
        result.put("sn", student.getSn());
        result.put("qq", student.getQq());
        result.put("demands", getStudentDemands(id.longValue()));

        return result;
    }

    /**
     * 获取学生的物资需求列表
     */
    private List<Map<String, Object>> getStudentDemands(Long studentId) {
        List<MaterialDemand> demands = demandRepository.findAll().stream()
                .filter(d -> d.getChildId().equals(studentId))
                .filter(d -> d.getStatus() == MaterialDemand.DemandStatus.ACTIVE)
                .filter(d -> !d.isFullyFulfilled())
                .collect(Collectors.toList());

        return demands.stream().map(d -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", d.getId());
            item.put("category", d.getCategory());
            item.put("categoryDetail", d.getCategoryDetail());
            item.put("name", d.getName());
            item.put("description", d.getDescription());
            item.put("requiredQuantity", d.getRequiredQuantity());
            item.put("receivedQuantity", d.getReceivedQuantity());
            item.put("urgencyLevel", d.getUrgencyLevel());
            item.put("fulfillmentRate", d.getFulfillmentRate());
            return item;
        }).collect(Collectors.toList());
    }

    /**
     * 生成推荐理由（基于推荐算法）
     */
    private String generateRecommendationReason(Student student, String category) {
        List<String> reasons = new ArrayList<>();

        // 1. 紧急程度说明
        if (student.hasUrgentNeed()) {
            if (student.getFamilyIncomeLevel() == Student.FamilyIncomeLevel.LOW) {
                reasons.add("家庭经济困难，生活条件艰苦");
            }
            if (student.getUrgentNeed() != null && student.getUrgentNeed().contains("急需")) {
                reasons.add("有紧急物资需求");
            }
        }

        // 2. 推荐权重说明
        if (student.isHighPriority()) {
            reasons.add("综合评分高，值得优先帮扶");
        }

        // 3. 需求匹配度说明
        if (category != null && !category.isEmpty()) {
            List<MaterialDemand> matchingDemands = demandRepository.findAll().stream()
                    .filter(d -> d.getChildId().equals(student.getId().longValue()))
                    .filter(d -> category.equals(d.getCategory()))
                    .filter(d -> d.getStatus() == MaterialDemand.DemandStatus.ACTIVE)
                    .collect(Collectors.toList());

            if (!matchingDemands.isEmpty()) {
                reasons.add("您捐赠的" + category + "正是孩子急需的");
            }
        }

        // 4. 帮扶效果预期
        if (student.getRecommendationWeight().compareTo(new BigDecimal("80")) >= 0) {
            reasons.add("您的帮助将显著改善学习条件");
        }

        // 如果没有具体理由，使用默认推荐
        if (reasons.isEmpty()) {
            return "孩子需要关爱，您的帮助将温暖他们的心灵";
        }

        return String.join("；", reasons);
    }

    /**
     * 获取统计数据（用于筛选器）
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // 年级统计
        List<Object[]> yearStats = studentRepository.countByYear();
        Map<Integer, Long> yearCount = yearStats.stream()
                .collect(Collectors.toMap(
                        arr -> (Integer) arr[0],
                        arr -> (Long) arr[1]
                ));
        stats.put("yearStats", yearCount);

        // 收入水平统计
        List<Object[]> incomeStats = studentRepository.countByFamilyIncomeLevel();
        Map<String, Long> incomeCount = incomeStats.stream()
                .filter(arr -> arr[0] != null)  // 过滤掉空值
                .collect(Collectors.toMap(
                        arr -> arr[0].toString(),
                        arr -> (Long) arr[1]
                ));
        stats.put("incomeStats", incomeCount);

        // 学生总数
        stats.put("totalCount", studentRepository.count());

        // 高优先级学生数
        stats.put("highPriorityCount", studentRepository.findHighPriorityStudents().size());

        // 平均推荐权重
        stats.put("averageWeight", studentRepository.getAverageRecommendationWeight());

        return stats;
    }

    /**
     * 搜索学生
     */
    @Transactional(readOnly = true)
    public List<Student> searchStudents(String keyword) {
        return studentRepository.searchStudents(keyword);
    }
}
