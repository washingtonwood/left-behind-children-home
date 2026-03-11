package com.shouhutongxing.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 帮扶进度记录实体
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Data
@Entity
@Table(name = "help_progress")
public class HelpProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 儿童ID（关联student表）
     */
    @Column(name = "child_id", nullable = false)
    private Long childId;

    /**
     * 项目ID
     */
    @Column(name = "project_id")
    private Long projectId;

    /**
     * 关联的捐赠ID
     */
    @Column(name = "donation_id")
    private Long donationId;

    /**
     * 活动类型
     */
    @Column(name = "activity_type", nullable = false, length = 50)
    private String activityType;

    /**
     * 活动标题
     */
    @Column(name = "activity_title", nullable = false, length = 200)
    private String activityTitle;

    /**
     * 活动详细描述
     */
    @Column(name = "activity_description", nullable = false, columnDefinition = "TEXT")
    private String activityDescription;

    /**
     * 参与志愿者ID列表（逗号分隔）
     */
    @Column(name = "volunteer_ids", columnDefinition = "TEXT")
    private String volunteerIds;

    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ProgressStatus status = ProgressStatus.PENDING_REVIEW;

    /**
     * 审核人ID
     */
    @Column(name = "reviewer_id")
    private Long reviewerId;

    /**
     * 审核时间
     */
    @Column(name = "review_time")
    private LocalDateTime reviewTime;

    /**
     * 审核意见
     */
    @Column(name = "review_note", length = 500)
    private String reviewNote;

    /**
     * 活动日期
     */
    @Column(name = "activity_date", nullable = false)
    private LocalDate activityDate;

    /**
     * 效果总结
     */
    @Column(name = "outcome_summary", columnDefinition = "TEXT")
    private String outcomeSummary;

    /**
     * 创建时间
     */
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    /**
     * 发布时间
     */
    @Column(name = "published_time")
    private LocalDateTime publishedTime;

    /**
     * 进度状态枚举
     */
    public enum ProgressStatus {
        DRAFT,           // 草稿
        PENDING_REVIEW,  // 待审核
        PUBLISHED,       // 已发布
        REJECTED         // 已拒绝
    }

    /**
     * 活动类型常量
     */
    public static final class ActivityType {
        public static final String HOME_VISIT = "home_visit";
        public static final String TUTORING = "tutoring";
        public static final String PSYCHOLOGICAL = "psychological";
        public static final String MATERIAL_DELIVERY = "material_delivery";
        public static final String ACTIVITY = "activity";
        public static final String OTHER = "other";
    }

    /**
     * JPA 生命周期回调
     */
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }

    /**
     * 判断是否已发布
     */
    public boolean isPublished() {
        return status == ProgressStatus.PUBLISHED;
    }

    /**
     * 判断是否可以编辑
     */
    public boolean isEditable() {
        return status == ProgressStatus.DRAFT || status == ProgressStatus.REJECTED;
    }

    /**
     * 判断是否有关联捐赠
     */
    public boolean hasDonation() {
        return donationId != null;
    }
}
