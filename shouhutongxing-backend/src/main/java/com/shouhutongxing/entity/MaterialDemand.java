package com.shouhutongxing.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 物资需求实体
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Data
@Entity
@Table(name = "material_demand")
public class MaterialDemand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 儿童ID（关联student表）
     */
    @Column(name = "child_id", nullable = false)
    private Long childId;

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
     * 需求描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 需求数量
     */
    @Column(name = "required_quantity", nullable = false)
    private Integer requiredQuantity;

    /**
     * 已接收数量
     */
    @Column(name = "received_quantity", nullable = false)
    private Integer receivedQuantity = 0;

    /**
     * 紧急程度
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "urgency_level", nullable = false, length = 10)
    private UrgencyLevel urgencyLevel = UrgencyLevel.NORMAL;

    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private DemandStatus status = DemandStatus.ACTIVE;

    /**
     * 需求截止日期
     */
    @Column(name = "deadline")
    private LocalDate deadline;

    /**
     * 创建时间
     */
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @Column(name = "updated_time", nullable = false)
    private LocalDateTime updatedTime;

    /**
     * 紧急程度枚举
     */
    public enum UrgencyLevel {
        URGENT,  // 紧急
        NORMAL,  // 一般
        STOCK    // 储备
    }

    /**
     * 需求状态枚举
     */
    public enum DemandStatus {
        ACTIVE,    // 活跃
        PAUSED,    // 暂停
        COMPLETED  // 已完成
    }

    /**
     * JPA 生命周期回调
     */
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }

    /**
     * 计算需求满足率
     */
    public double getFulfillmentRate() {
        if (requiredQuantity == 0) {
            return 0.0;
        }
        return (double) receivedQuantity / requiredQuantity * 100;
    }

    /**
     * 是否已完全满足
     */
    public boolean isFullyFulfilled() {
        return receivedQuantity >= requiredQuantity;
    }
}
