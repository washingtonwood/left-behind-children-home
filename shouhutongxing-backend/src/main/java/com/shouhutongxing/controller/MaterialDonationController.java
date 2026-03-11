package com.shouhutongxing.controller;

import com.shouhutongxing.dto.MaterialDonationRequest;
import com.shouhutongxing.dto.Result;
import com.shouhutongxing.entity.MaterialDonation;
import com.shouhutongxing.entity.MaterialItem;
import com.shouhutongxing.repository.MaterialItemRepository;
import com.shouhutongxing.service.MaterialDonationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物资捐赠 Controller
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Slf4j
@RestController
@RequestMapping("/material-donations")
@RequiredArgsConstructor
@Tag(name = "物资捐赠管理", description = "物资捐赠登记、查询等接口")
public class MaterialDonationController {

    private final MaterialDonationService donationService;
    private final MaterialItemRepository itemRepository;

    /**
     * 创建物资捐赠
     */
    @PostMapping
    @Operation(summary = "创建物资捐赠", description = "登记新的物资捐赠信息，支持批量添加物资")
    public Result<Map<String, Object>> createDonation(
            @Valid @RequestBody MaterialDonationRequest request) {
        try {
            log.info("接收到物资捐赠请求，捐赠者：{}，物资数量：{}",
                    request.getDonorName(), request.getItems().size());

            MaterialDonation donation = donationService.createDonation(request);

            // 构建响应数据
            Map<String, Object> data = new HashMap<>();
            data.put("donationId", donation.getId());
            data.put("donationNo", donation.getDonationNo());
            data.put("donorName", donation.getDonorName());
            data.put("totalItems", donation.getTotalItems());
            data.put("estimatedValue", donation.getEstimatedValue());
            data.put("donationMethod", donation.getDonationMethod());
            data.put("receiverAddress", donation.getReceiverAddress());
            data.put("status", donation.getStatus());
            data.put("createdTime", donation.getCreatedTime());

            return Result.success("捐赠登记成功，捐赠编号：" + donation.getDonationNo(), data);
        } catch (IllegalArgumentException e) {
            log.warn("捐赠登记失败：{}", e.getMessage());
            return Result.validationError(e.getMessage());
        } catch (Exception e) {
            log.error("捐赠登记异常", e);
            return Result.error("系统异常，请稍后重试");
        }
    }

    /**
     * 根据捐赠编号查询
     */
    @GetMapping("/{donationNo}")
    @Operation(summary = "查询捐赠详情", description = "根据捐赠编号查询捐赠记录和物资明细")
    public Result<Map<String, Object>> getByDonationNo(
            @Parameter(description = "捐赠编号") @PathVariable String donationNo) {
        try {
            MaterialDonation donation = donationService.getByDonationNo(donationNo);
            List<MaterialItem> items = itemRepository.findByDonationId(donation.getId());

            Map<String, Object> data = new HashMap<>();
            data.put("donation", donation);
            data.put("items", items);

            return Result.success(data);
        } catch (IllegalArgumentException e) {
            return Result.error("捐赠记录不存在：" + donationNo);
        }
    }

    /**
     * 根据捐赠者ID查询捐赠列表
     */
    @GetMapping("/donor/{donorId}")
    @Operation(summary = "查询捐赠者捐赠记录", description = "根据捐赠者ID查询其所有捐赠记录")
    public Result<List<MaterialDonation>> getByDonorId(
            @Parameter(description = "捐赠者ID") @PathVariable Long donorId) {
        List<MaterialDonation> donations = donationService.getByDonorId(donorId);
        return Result.success(donations);
    }

    /**
     * 获取支持的物资分类
     */
    @GetMapping("/categories")
    @Operation(summary = "获取物资分类", description = "获取系统支持的所有物资分类")
    public Result<Map<String, String[]>> getCategories() {
        Map<String, String[]> categories = new HashMap<>();

        categories.put("衣物", new String[]{"春夏装", "秋冬装", "鞋帽", "配饰"});
        categories.put("书籍", new String[]{"课本", "故事书", "科普书", "工具书"});
        categories.put("学习用品", new String[]{"书包", "文具", "本册", "辅导材料"});
        categories.put("生活用品", new String[]{"被褥", "洗漱用品", "餐具"});
        categories.put("体育用品", new String[]{"球类", "运动器材"});
        categories.put("其他物资", new String[]{"其他"});

        return Result.success(categories);
    }

    /**
     * 获取新旧程度选项
     */
    @GetMapping("/condition-levels")
    @Operation(summary = "获取新旧程度选项", description = "获取物资新旧程度枚举值")
    public Result<Map<String, String>> getConditionLevels() {
        Map<String, String> levels = new HashMap<>();
        levels.put("NEW", "全新");
        levels.put("LIKE_NEW", "九成新");
        levels.put("GOOD", "良好");
        levels.put("FAIR", "一般");
        levels.put("POOR", "较差");
        return Result.success(levels);
    }

    /**
     * 获取支持的上门取件城市
     */
    @GetMapping("/pickup-cities")
    @Operation(summary = "获取上门取件城市", description = "获取支持上门取件服务的城市列表")
    public Result<List<String>> getPickupCities() {
        List<String> cities = List.of("北京", "上海", "广州", "深圳", "杭州", "南京", "成都", "武汉");
        return Result.success(cities);
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查物资捐赠模块是否正常运行")
    public Result<String> health() {
        return Result.success("物资捐赠模块运行正常");
    }

    /**
     * 修复数据枚举值（临时接口）
     */
    @PostMapping("/fix-data")
    @Operation(summary = "修复数据枚举值", description = "将数据库中的小写枚举值转换为大写")
    public Result<String> fixEnumData() {
        try {
            int updated = donationService.fixEnumValues();
            return Result.success("成功修复 " + updated + " 条记录");
        } catch (Exception e) {
            log.error("修复数据失败", e);
            return Result.error("修复失败：" + e.getMessage());
        }
    }

    /**
     * 调试：查询所有捐赠记录的枚举值
     */
    @GetMapping("/debug/enum-values")
    @Operation(summary = "调试：查询枚举值", description = "查询数据库中的枚举值情况")
    public Result<String> debugEnumValues() {
        try {
            String debugInfo = donationService.debugEnumValues();
            return Result.success(debugInfo);
        } catch (Exception e) {
            log.error("查询失败", e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 调试：查看表结构
     */
    @GetMapping("/debug/table-structure")
    @Operation(summary = "调试：查看表结构", description = "查看数据库表的创建语句")
    public Result<String> debugTableStructure() {
        try {
            String structure = donationService.getTableStructure();
            return Result.success(structure);
        } catch (Exception e) {
            log.error("查询失败", e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }
}
