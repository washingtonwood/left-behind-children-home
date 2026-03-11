package com.shouhutongxing.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 资金捐赠请求DTO
 */
@Data
public class FundDonationRequest {

    /**
     * 捐赠项目类型
     */
    @NotBlank(message = "捐赠项目不能为空")
    private String projectType;

    /**
     * 捐赠金额
     */
    @NotNull(message = "捐赠金额不能为空")
    @DecimalMin(value = "0.01", message = "捐赠金额必须大于0")
    private BigDecimal amount;

    /**
     * 捐赠者姓名
     */
    @NotBlank(message = "捐赠者姓名不能为空")
    @Size(max = 100, message = "姓名长度不能超过100个字符")
    private String donorName;

    /**
     * 捐赠者手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "请输入正确的手机号")
    private String donorPhone;

    /**
     * 捐赠者邮箱
     */
    @Email(message = "请输入正确的邮箱格式")
    private String donorEmail;

    /**
     * 支付方式
     */
    @NotBlank(message = "支付方式不能为空")
    private String paymentMethod;

    /**
     * 定期捐赠类型
     */
    private String recurringType;

    /**
     * 留言
     */
    @Size(max = 500, message = "留言长度不能超过500个字符")
    private String message;

    /**
     * 是否匿名
     */
    private Boolean isAnonymous;
}
