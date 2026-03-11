package com.shouhutongxing.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资金捐赠响应DTO
 */
@Data
public class FundDonationResponse {

    /**
     * 捐赠ID
     */
    private Long id;

    /**
     * 捐赠编号
     */
    private String donationNo;

    /**
     * 捐赠项目类型
     */
    private String projectType;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 捐赠金额
     */
    private BigDecimal amount;

    /**
     * 捐赠者姓名
     */
    private String donorName;

    /**
     * 捐赠时间
     */
    private LocalDateTime createdAt;

    /**
     * 项目统计
     */
    private ProjectStatistics projectStatistics;

    @Data
    public static class ProjectStatistics {
        /**
         * 项目类型
         */
        private String projectType;

        /**
         * 已筹集金额
         */
        private BigDecimal raisedAmount;

        /**
         * 目标金额
         */
        private BigDecimal targetAmount;

        /**
         * 完成百分比
         */
        private Integer percentage;

        /**
         * 捐赠人数
         */
        private Integer donorCount;
    }
}
