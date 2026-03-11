package com.shouhutongxing.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 资金捐赠实体
 */
@Entity
@Table(name = "fund_donation")
@Data
public class FundDonation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 捐赠编号
     */
    @Column(name = "donation_no", unique = true, nullable = false, length = 50)
    private String donationNo;

    /**
     * 捐赠项目类型：EDUCATION-助学, MEDICAL-医疗, LIVING-生活改善
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "project_type", nullable = false, length = 20)
    private ProjectType projectType;

    /**
     * 捐赠金额
     */
    @Column(name = "amount", nullable = false)
    private Double amount;

    /**
     * 捐赠者姓名
     */
    @Column(name = "donor_name", nullable = false, length = 100)
    private String donorName;

    /**
     * 捐赠者手机号
     */
    @Column(name = "donor_phone", nullable = false, length = 20)
    private String donorPhone;

    /**
     * 捐赠者邮箱
     */
    @Column(name = "donor_email", length = 100)
    private String donorEmail;

    /**
     * 支付方式：WECHAT-微信, ALIPAY-支付宝, BANK-银行卡
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    /**
     * 是否定期捐赠：NONE-否, MONTHLY-月度, QUARTERLY-季度, YEARLY-年度
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "recurring_type", length = 20)
    private RecurringType recurringType;

    /**
     * 留言
     */
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    /**
     * 是否匿名
     */
    @Column(name = "is_anonymous", nullable = false)
    private Boolean isAnonymous;

    /**
     * 捐赠状态：PENDING-待支付, SUCCESS-成功, FAILED-失败, REFUNDED-已退款
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private DonationStatus status;

    /**
     * 交易流水号
     */
    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    /**
     * 支付时间
     */
    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    /**
     * 创建时间
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (donationNo == null) {
            donationNo = generateDonationNo();
        }
        if (status == null) {
            status = DonationStatus.PENDING;
        }
        if (isAnonymous == null) {
            isAnonymous = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    private String generateDonationNo() {
        return "DN" + System.currentTimeMillis();
    }

    /**
     * 捐赠项目类型枚举
     */
    public enum ProjectType {
        EDUCATION("助学计划"),
        MEDICAL("医疗救助"),
        LIVING("生活改善");

        private final String description;

        ProjectType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 支付方式枚举
     */
    public enum PaymentMethod {
        WECHAT("微信支付"),
        ALIPAY("支付宝"),
        BANK("银行卡");

        private final String description;

        PaymentMethod(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 定期捐赠类型枚举
     */
    public enum RecurringType {
        NONE("单次捐赠"),
        MONTHLY("月度捐赠"),
        QUARTERLY("季度捐赠"),
        YEARLY("年度捐赠");

        private final String description;

        RecurringType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 捐赠状态枚举
     */
    public enum DonationStatus {
        PENDING("待支付"),
        SUCCESS("成功"),
        FAILED("失败"),
        REFUNDED("已退款");

        private final String description;

        DonationStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
