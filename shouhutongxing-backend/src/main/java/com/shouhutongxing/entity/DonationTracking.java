package com.shouhutongxing.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 捐赠追踪记录实体
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Data
@Entity
@Table(name = "donation_tracking")
public class DonationTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 捐赠ID（关联material_donation表）
     */
    @Column(name = "donation_id", nullable = false)
    private Long donationId;

    /**
     * 节点类型
     */
    @Column(name = "node_type", nullable = false, length = 50)
    private String nodeType;

    /**
     * 节点名称
     */
    @Column(name = "node_name", nullable = false, length = 100)
    private String nodeName;

    /**
     * 节点描述
     */
    @Column(name = "node_description", length = 500)
    private String nodeDescription;

    /**
     * 操作员ID
     */
    @Column(name = "operator_id")
    private Long operatorId;

    /**
     * 操作员姓名
     */
    @Column(name = "operator_name", length = 100)
    private String operatorName;

    /**
     * 照片URL（多个用逗号分隔）
     */
    @Column(name = "photo_urls", columnDefinition = "TEXT")
    private String photoUrls;

    /**
     * 视频URL
     */
    @Column(name = "video_url", length = 500)
    private String videoUrl;

    /**
     * 物流状态描述
     */
    @Column(name = "logistics_status", length = 200)
    private String logisticsStatus;

    /**
     * 物流位置
     */
    @Column(name = "logistics_location", length = 200)
    private String logisticsLocation;

    /**
     * 备注说明
     */
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    /**
     * 创建时间
     */
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    /**
     * 节点类型常量
     */
    public static final class NodeType {
        public static final String CREATED = "created";
        public static final String PAID = "paid";
        public static final String SHIPPED = "shipped";
        public static final String RECEIVED = "received";
        public static final String INSPECTING = "inspecting";
        public static final String STOCKED = "stocked";
        public static final String ALLOCATED = "allocated";
        public static final String DELIVERED = "delivered";
        public static final String SIGNED = "signed";
        public static final String COMPLETED = "completed";
        public static final String ABNORMAL = "abnormal";
    }

    /**
     * JPA 生命周期回调
     */
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }

    /**
     * 判断是否有关键节点照片
     */
    public boolean hasPhotos() {
        return photoUrls != null && !photoUrls.trim().isEmpty();
    }

    /**
     * 判断是否有关键节点视频
     */
    public boolean hasVideo() {
        return videoUrl != null && !videoUrl.trim().isEmpty();
    }
}
