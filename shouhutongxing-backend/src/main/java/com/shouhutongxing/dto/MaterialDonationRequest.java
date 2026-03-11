package com.shouhutongxing.dto;

import com.shouhutongxing.entity.MaterialDonation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

/**
 * 物资捐赠登记请求 DTO
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Data
public class MaterialDonationRequest {

    /**
     * 捐赠者ID（可选，用户登录后有值）
     */
    private Long donorId;

    /**
     * 捐赠者姓名
     */
    @NotBlank(message = "捐赠者姓名不能为空")
    private String donorName;

    /**
     * 捐赠者电话
     */
    @NotBlank(message = "捐赠者电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String donorPhone;

    /**
     * 捐赠者邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String donorEmail;

    /**
     * 捐赠方式（MAIL-邮寄，PICKUP-上门取件）
     */
    @NotNull(message = "捐赠方式不能为空")
    private MaterialDonation.DonationMethod donationMethod;

    /**
     * 捐赠者地址（上门取件必填）
     */
    private String donorAddress;

    /**
     * 备注
     */
    private String remark;

    /**
     * 物资明细列表
     */
    @NotNull(message = "物资明细不能为空")
    private List<MaterialItemRequest> items;
}
