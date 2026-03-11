package com.shouhutongxing.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 受助学生实体（扩展自现有student表）
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Data
@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 学号
     */
    @Column(name = "sn", length = 15)
    private String sn;

    /**
     * 姓名
     */
    @Column(name = "name", length = 30)
    private String name;

    /**
     * 性别
     */
    @Column(name = "gender", length = 2)
    private String gender;

    /**
     * 照片
     */
    @Column(name = "photo", length = 255)
    private String photo;

    /**
     * 年级
     */
    @Column(name = "year")
    private Integer year;

    /**
     * 班级
     */
    @Column(name = "clazz", length = 30)
    private String clazz;

    /**
     * QQ号
     */
    @Column(name = "qq", length = 15)
    private String qq;

    // ==================== 新增字段（物资捐赠系统） ====================

    /**
     * 推荐权重（0-100）
     * 默认值：50.00
     */
    @Column(name = "recommendation_weight", nullable = false, precision = 5, scale = 2)
    private BigDecimal recommendationWeight = new BigDecimal("50.00");

    /**
     * 家庭收入水平
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "family_income_level", length = 10)
    private FamilyIncomeLevel familyIncomeLevel;

    /**
     * 家庭情况描述
     */
    @Column(name = "family_condition", length = 500)
    private String familyCondition;

    /**
     * 紧急需求描述
     */
    @Column(name = "urgent_need", columnDefinition = "TEXT")
    private String urgentNeed;

    /**
     * 家庭收入水平枚举
     */
    public enum FamilyIncomeLevel {
        LOW,     // 低
        MEDIUM,  // 中
        HIGH     // 高
    }

    /**
     * 判断是否为高优先级推荐（权重>=85）
     */
    public boolean isHighPriority() {
        return recommendationWeight.compareTo(new BigDecimal("85")) >= 0;
    }

    /**
     * 判断是否为紧急需求（家庭收入低或有关键词）
     */
    public boolean hasUrgentNeed() {
        if (familyIncomeLevel == FamilyIncomeLevel.LOW) {
            return true;
        }
        if (urgentNeed != null && urgentNeed.contains("急需")) {
            return true;
        }
        return false;
    }

    /**
     * 获取推荐等级
     */
    public String getRecommendationLevel() {
        if (recommendationWeight.compareTo(new BigDecimal("90")) >= 0) {
            return "非常推荐";
        } else if (recommendationWeight.compareTo(new BigDecimal("80")) >= 0) {
            return "强烈推荐";
        } else if (recommendationWeight.compareTo(new BigDecimal("70")) >= 0) {
            return "推荐";
        } else if (recommendationWeight.compareTo(new BigDecimal("60")) >= 0) {
            return "可以考虑";
        } else {
            return "暂不推荐";
        }
    }
}
