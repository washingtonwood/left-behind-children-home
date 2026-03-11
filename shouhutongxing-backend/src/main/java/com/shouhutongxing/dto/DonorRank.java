package com.shouhutongxing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 捐赠者排行榜DTO
 */
@Data
@AllArgsConstructor
public class DonorRank {
    /**
     * 捐赠者姓名
     */
    private String donorName;

    /**
     * 捐赠总额
     */
    private BigDecimal totalAmount;
}
