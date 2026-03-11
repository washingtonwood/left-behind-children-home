package com.shouhutongxing.dto;

import com.shouhutongxing.entity.MaterialItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 物资明细请求 DTO
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Data
public class MaterialItemRequest {

    /**
     * 需求ID（可选，用于按需捐赠）
     */
    private Long demandId;

    /**
     * 物资分类（衣物、书籍、学习用品、生活用品、体育用品、其他）
     */
    @NotBlank(message = "物资分类不能为空")
    private String category;

    /**
     * 详细分类
     */
    private String categoryDetail;

    /**
     * 物资名称
     */
    @NotBlank(message = "物资名称不能为空")
    private String name;

    /**
     * 物资描述
     */
    private String description;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量必须大于0")
    private Integer quantity;

    /**
     * 单位
     */
    private String unit;

    /**
     * 新旧程度（NEW, LIKE_NEW, GOOD, FAIR, POOR）
     */
    @NotNull(message = "新旧程度不能为空")
    private MaterialItem.ConditionLevel conditionLevel;

    /**
     * 单件估算价值（元）
     */
    @NotNull(message = "估算价值不能为空")
    @Min(value = 0, message = "估算价值不能为负数")
    private BigDecimal estimatedValue;

    /**
     * 物资照片URL（多个用逗号分隔）
     */
    private String photoUrls;
}
