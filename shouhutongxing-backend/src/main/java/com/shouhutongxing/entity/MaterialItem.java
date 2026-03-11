package com.shouhutongxing.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 物资明细实体
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Data
@Entity
@Table(name = "material_item")
public class MaterialItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 捐赠ID（关联material_donation表）
     */
    @Column(name = "donation_id", nullable = false)
    private Long donationId;

    /**
     * 需求ID（关联material_demand表）
     */
    @Column(name = "demand_id")
    private Long demandId;

    /**
     * 物资分类
     */
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    /**
     * 详细分类
     */
    @Column(name = "category_detail", length = 100)
    private String categoryDetail;

    /**
     * 物资名称
     */
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    /**
     * 物资描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 数量
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * 单位
     */
    @Column(name = "unit", length = 20)
    private String unit;

    /**
     * 新旧程度
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "condition_level", nullable = false, length = 20)
    private ConditionLevel conditionLevel = ConditionLevel.GOOD;

    /**
     * 单件估算价值（元）
     */
    @Column(name = "estimated_value", precision = 10, scale = 2)
    private BigDecimal estimatedValue;

    /**
     * 物资照片URL（多个用逗号分隔）
     */
    @Column(name = "photo_urls", columnDefinition = "TEXT")
    private String photoUrls;

    /**
     * 检验结果
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "inspection_result", length = 20)
    private InspectionResult inspectionResult = InspectionResult.PENDING;

    /**
     * 检验备注
     */
    @Column(name = "inspection_note", length = 500)
    private String inspectionNote;

    /**
     * 创建时间
     */
    @Column(name = "created_time", nullable = false, updatable = false)
    private java.time.LocalDateTime createdTime;

    /**
     * 新旧程度枚举
     */
    public enum ConditionLevel {
        NEW,       // 全新
        LIKE_NEW,  // 九成新
        GOOD,      // 良好
        FAIR,      // 一般
        POOR       // 较差
    }

    /**
     * 检验结果枚举
     */
    public enum InspectionResult {
        PENDING,       // 待检验
        QUALIFIED,     // 合格
        UNQUALIFIED,   // 不合格
        NEED_PROCESS   // 需处理
    }

    /**
     * JPA 生命周期回调：创建前
     */
    @PrePersist
    protected void onCreate() {
        createdTime = java.time.LocalDateTime.now();
    }
}
