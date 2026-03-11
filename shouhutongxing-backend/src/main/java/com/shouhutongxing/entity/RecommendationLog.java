package com.shouhutongxing.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 推荐日志实体
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Data
@Entity
@Table(name = "recommendation_log")
public class RecommendationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 儿童ID（关联student表）
     */
    @Column(name = "child_id", nullable = false)
    private Long childId;

    /**
     * 捐赠者ID（关联user表）
     */
    @Column(name = "donor_id")
    private Long donorId;

    /**
     * 推荐来源
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false, length = 20)
    private SourceType sourceType;

    /**
     * 推荐参数（JSON格式）
     */
    @Column(name = "source_params", columnDefinition = "TEXT")
    private String sourceParams;

    /**
     * 推荐得分
     */
    @Column(name = "score", nullable = false, precision = 5, scale = 2)
    private BigDecimal score;

    /**
     * 推荐排名
     */
    @Column(name = "rank", nullable = false)
    private Integer rank;

    /**
     * 推荐理由
     */
    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    /**
     * 用户操作
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "action_taken", length = 20)
    private ActionTaken actionTaken = ActionTaken.VIEWED;

    /**
     * 操作时间
     */
    @Column(name = "action_time")
    private LocalDateTime actionTime;

    /**
     * 创建时间
     */
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    /**
     * 推荐来源枚举
     */
    public enum SourceType {
        HOMEPAGE,  // 首页
        SEARCH,    // 搜索
        CATEGORY,  // 分类
        REGION     // 地区
    }

    /**
     * 用户操作枚举
     */
    public enum ActionTaken {
        VIEWED,   // 已查看
        DONATED,  // 已捐赠
        IGNORED   // 已忽略
    }

    /**
     * JPA 生命周期回调
     */
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }

    /**
     * 判断是否为高推荐（得分>=85）
     */
    public boolean isHighRecommendation() {
        return score.compareTo(new BigDecimal("85")) >= 0;
    }

    /**
     * 判断是否已转化
     */
    public boolean isConverted() {
        return actionTaken == ActionTaken.DONATED;
    }
}
