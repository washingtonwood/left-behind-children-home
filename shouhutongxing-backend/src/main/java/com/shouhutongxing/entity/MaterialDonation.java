package com.shouhutongxing.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 物资捐赠记录实体
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Data
@Entity
@Table(name = "material_donation")
public class MaterialDonation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 捐赠编号（如：MD202501250001）
     */
    @Column(name = "donation_no", nullable = false, unique = true, length = 50)
    private String donationNo;

    /**
     * 捐赠者ID（关联user表）
     */
    @Column(name = "donor_id")
    private Long donorId;

    /**
     * 捐赠者姓名
     */
    @Column(name = "donor_name", length = 100)
    private String donorName;

    /**
     * 捐赠者电话
     */
    @Column(name = "donor_phone", length = 20)
    private String donorPhone;

    /**
     * 捐赠者邮箱
     */
    @Column(name = "donor_email", length = 100)
    private String donorEmail;

    /**
     * 捐赠方式：mail-邮寄，pickup-上门取件
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "donation_method", nullable = false, length = 10)
    private DonationMethod donationMethod;

    /**
     * 快递公司
     */
    @Column(name = "express_company", length = 50)
    private String expressCompany;

    /**
     * 快递单号
     */
    @Column(name = "express_no", length = 50)
    private String expressNo;

    /**
     * 捐赠者地址（用于上门取件）
     */
    @Column(name = "donor_address", columnDefinition = "TEXT")
    private String donorAddress;

    /**
     * 接收地址
     */
    @Column(name = "receiver_address", columnDefinition = "TEXT")
    private String receiverAddress;

    /**
     * 物资总件数
     */
    @Column(name = "total_items", nullable = false)
    private Integer totalItems = 0;

    /**
     * 估算价值（元）
     */
    @Column(name = "estimated_value", precision = 10, scale = 2)
    private BigDecimal estimatedValue;

    /**
     * 捐赠状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private DonationStatus status = DonationStatus.CREATED;

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
     * 备注
     */
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    /**
     * 捐赠方式枚举
     */
    public enum DonationMethod {
        MAIL,   // 邮寄
        PICKUP  // 上门取件
    }

    /**
     * 捐赠状态枚举
     */
    public enum DonationStatus {
        CREATED,      // 已创建
        PAID,         // 已支付
        SHIPPED,      // 已发货
        RECEIVED,     // 已接收
        INSPECTING,   // 检验中
        STOCKED,      // 已入库
        ALLOCATED,    // 分配中
        DELIVERED,    // 已发货（给受助儿童）
        SIGNED,       // 已签收
        COMPLETED,    // 已完成
        ABNORMAL      // 异常
    }

    /**
     * JPA 生命周期回调：创建前
     */
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
    }

    /**
     * JPA 生命周期回调：更新前
     */
    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }
}
