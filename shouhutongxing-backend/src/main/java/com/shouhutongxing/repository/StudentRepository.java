package com.shouhutongxing.repository;

import com.shouhutongxing.entity.Student;
import com.shouhutongxing.entity.Student.FamilyIncomeLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 受助学生 Repository
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    /**
     * 根据学号查询
     */
    Student findBySn(String sn);

    /**
     * 根据姓名模糊查询
     */
    List<Student> findByNameContaining(String name);

    /**
     * 根据年级查询
     */
    List<Student> findByYear(Integer year);

    /**
     * 根据班级查询
     */
    List<Student> findByClazz(String clazz);

    /**
     * 根据年级和班级查询
     */
    List<Student> findByYearAndClazz(Integer year, String clazz);

    /**
     * 根据家庭收入水平查询
     */
    List<Student> findByFamilyIncomeLevelOrderByRecommendationWeightDesc(FamilyIncomeLevel familyIncomeLevel);

    /**
     * 查询高优先级学生（推荐权重>=85）
     */
    @Query("SELECT s FROM Student s WHERE s.recommendationWeight >= 85 ORDER BY s.recommendationWeight DESC")
    List<Student> findHighPriorityStudents();

    /**
     * 查询低收入家庭学生
     */
    List<Student> findByFamilyIncomeLevel(FamilyIncomeLevel familyIncomeLevel);

    /**
     * 查询有紧急需求的学生
     */
    @Query("SELECT s FROM Student s WHERE s.urgentNeed IS NOT NULL AND s.urgentNeed LIKE CONCAT('%', :keyword, '%')")
    List<Student> findByUrgentNeedContaining(@Param("keyword") String keyword);

    /**
     * 根据推荐权重范围查询
     */
    @Query("SELECT s FROM Student s WHERE s.recommendationWeight BETWEEN :minWeight AND :maxWeight ORDER BY s.recommendationWeight DESC")
    List<Student> findByRecommendationWeightBetween(@Param("minWeight") BigDecimal minWeight, @Param("maxWeight") BigDecimal maxWeight);

    /**
     * 根据多个条件推荐（推荐算法核心方法）
     */
    @Query("SELECT s FROM Student s WHERE " +
           "(:familyIncomeLevel IS NULL OR s.familyIncomeLevel = :familyIncomeLevel) AND " +
           "(:minWeight IS NULL OR s.recommendationWeight >= :minWeight) " +
           "ORDER BY s.recommendationWeight DESC")
    List<Student> recommendStudents(@Param("familyIncomeLevel") FamilyIncomeLevel familyIncomeLevel,
                                     @Param("minWeight") BigDecimal minWeight);

    /**
     * 查询指定年级的学生（按推荐权重排序）
     */
    List<Student> findByYearOrderByRecommendationWeightDesc(Integer year);

    /**
     * 统计各年级的学生数量
     */
    @Query("SELECT s.year, COUNT(s) FROM Student s GROUP BY s.year")
    List<Object[]> countByYear();

    /**
     * 统计各班级的学生数量
     */
    @Query("SELECT s.clazz, COUNT(s) FROM Student s GROUP BY s.clazz")
    List<Object[]> countByClazz();

    /**
     * 统计各收入水平的学生数量
     */
    @Query("SELECT s.familyIncomeLevel, COUNT(s) FROM Student s GROUP BY s.familyIncomeLevel")
    List<Object[]> countByFamilyIncomeLevel();

    /**
     * 查询最近添加的学生
     */
    @Query("SELECT s FROM Student s ORDER BY s.id DESC")
    List<Student> findRecentStudents();

    /**
     * 根据姓名和年级查询
     */
    List<Student> findByNameContainingAndYear(String name, Integer year);

    /**
     * 全文搜索（姓名或家庭情况或紧急需求）
     */
    @Query("SELECT s FROM Student s WHERE " +
           "s.name LIKE %:keyword% OR " +
           "s.familyCondition LIKE %:keyword% OR " +
           "s.urgentNeed LIKE %:keyword%")
    List<Student> searchStudents(@Param("keyword") String keyword);

    /**
     * 查询需要帮扶的学生（家庭收入低或有关键词）
     */
    @Query("SELECT s FROM Student s WHERE " +
           "s.familyIncomeLevel = 'LOW' OR " +
           "s.urgentNeed LIKE '%急需%' OR " +
           "s.urgentNeed LIKE '%困难%' " +
           "ORDER BY s.recommendationWeight DESC")
    List<Student> findStudentsNeedingHelp();

    /**
     * 计算平均推荐权重
     */
    @Query("SELECT AVG(s.recommendationWeight) FROM Student s")
    Double getAverageRecommendationWeight();

    /**
     * 查询推荐权重最高的学生（Top N）
     */
    List<Student> findTop10ByOrderByRecommendationWeightDesc();

    /**
     * 统计学生总数
     */
    long count();

    /**
     * 检查学号是否存在
     */
    boolean existsBySn(String sn);
}
