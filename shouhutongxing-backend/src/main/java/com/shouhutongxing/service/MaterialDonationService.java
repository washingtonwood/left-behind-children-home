package com.shouhutongxing.service;

import com.shouhutongxing.dto.MaterialDonationRequest;
import com.shouhutongxing.dto.MaterialItemRequest;
import com.shouhutongxing.entity.MaterialDonation;
import com.shouhutongxing.entity.MaterialItem;
import com.shouhutongxing.repository.MaterialDonationRepository;
import com.shouhutongxing.repository.MaterialItemRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 物资捐赠服务
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialDonationService {

    private final MaterialDonationRepository donationRepository;
    private final MaterialItemRepository itemRepository;
    private final EntityManager entityManager;

    /**
     * 接收地址（可从配置文件读取）
     */
    private static final String RECEIVER_ADDRESS = "湖北省武汉市东湖生态旅游风景管理区黄家大湾1号";

    /**
     * 上门取件支持的城市列表
     */
    private static final List<String> PICKUP_SUPPORTED_CITIES = List.of(
            "北京", "上海", "广州", "深圳", "杭州", "南京", "成都", "武汉", "重庆"
    );

    /**
     * 创建物资捐赠记录
     *
     * @param request 捐赠请求
     * @return 创建的捐赠记录
     */
    @Transactional
    public MaterialDonation createDonation(MaterialDonationRequest request) {
        log.info("开始创建物资捐赠，捐赠者：{}", request.getDonorName());

        // 1. 验证捐赠信息
        validateDonationRequest(request);

        // 2. 创建捐赠记录
        MaterialDonation donation = new MaterialDonation();
        donation.setDonationNo(generateDonationNo());
        donation.setDonorId(request.getDonorId());
        donation.setDonorName(request.getDonorName());
        donation.setDonorPhone(request.getDonorPhone());
        donation.setDonorEmail(request.getDonorEmail());
        donation.setDonationMethod(request.getDonationMethod());
        donation.setDonorAddress(request.getDonorAddress());
        donation.setRemark(request.getRemark());
        donation.setStatus(MaterialDonation.DonationStatus.CREATED);

        // 3. 设置接收地址
        donation.setReceiverAddress(RECEIVER_ADDRESS);

        // 4. 处理物资明细
        List<MaterialItem> items = new ArrayList<>();
        int totalItems = 0;
        BigDecimal totalValue = BigDecimal.ZERO;

        for (MaterialItemRequest itemRequest : request.getItems()) {
            MaterialItem item = createMaterialItem(donation.getDonationNo(), itemRequest);
            items.add(item);
            totalItems += item.getQuantity();
            totalValue = totalValue.add(
                    item.getEstimatedValue().multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }

        // 5. 保存捐赠记录（先保存以获取ID）
        MaterialDonation savedDonation = donationRepository.save(donation);

        // 6. 保存物资明细
        for (MaterialItem item : items) {
            item.setDonationId(savedDonation.getId());
            itemRepository.save(item);
        }

        // 7. 更新捐赠统计信息
        savedDonation.setTotalItems(totalItems);
        savedDonation.setEstimatedValue(totalValue);
        donationRepository.save(savedDonation);

        log.info("物资捐赠创建成功，捐赠编号：{}，总件数：{}，估算价值：{}",
                savedDonation.getDonationNo(), totalItems, totalValue);

        return savedDonation;
    }

    /**
     * 验证捐赠请求
     */
    private void validateDonationRequest(MaterialDonationRequest request) {
        // 验证物资明细
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("至少需要添加一个物资明细");
        }

        if (request.getItems().size() > 50) {
            throw new IllegalArgumentException("单次捐赠最多支持50个物资明细");
        }

        // 验证上门取件
        if (request.getDonationMethod() == MaterialDonation.DonationMethod.PICKUP) {
            if (request.getDonorAddress() == null || request.getDonorAddress().trim().isEmpty()) {
                throw new IllegalArgumentException("上门取件需要填写详细地址");
            }
            // 检查城市是否支持上门取件
            boolean isCitySupported = PICKUP_SUPPORTED_CITIES.stream()
                    .anyMatch(city -> request.getDonorAddress().contains(city));
            if (!isCitySupported) {
                throw new IllegalArgumentException("抱歉，当前城市暂不支持上门取件服务，请选择邮寄方式");
            }
        }
    }

    /**
     * 创建物资明细
     */
    private MaterialItem createMaterialItem(String donationNo, MaterialItemRequest request) {
        MaterialItem item = new MaterialItem();
        item.setDemandId(request.getDemandId());
        item.setCategory(request.getCategory());
        item.setCategoryDetail(request.getCategoryDetail());
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setQuantity(request.getQuantity());
        item.setUnit(request.getUnit());
        item.setConditionLevel(request.getConditionLevel());
        item.setEstimatedValue(request.getEstimatedValue());
        item.setPhotoUrls(request.getPhotoUrls());
        item.setInspectionResult(MaterialItem.InspectionResult.PENDING);

        return item;
    }

    /**
     * 生成捐赠编号（格式：MD + 年月日 + 4位序号）
     */
    private String generateDonationNo() {
        String dateStr = java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "MD" + dateStr;

        // 查询今天已存在的最大捐赠编号
        List<MaterialDonation> todayDonations = donationRepository.findAll().stream()
                .filter(d -> d.getDonationNo() != null && d.getDonationNo().startsWith(prefix))
                .toList();

        int sequence = 1;
        if (!todayDonations.isEmpty()) {
            // 提取最大的序号
            int maxSequence = todayDonations.stream()
                    .map(d -> d.getDonationNo().substring(prefix.length()))
                    .mapToInt(Integer::parseInt)
                    .max()
                    .orElse(0);
            sequence = maxSequence + 1;
        }

        return String.format("MD%s%04d", dateStr, sequence);
    }

    /**
     * 根据捐赠编号查询捐赠记录
     */
    public MaterialDonation getByDonationNo(String donationNo) {
        MaterialDonation donation = donationRepository.findByDonationNo(donationNo);
        if (donation == null) {
            throw new IllegalArgumentException("捐赠记录不存在：" + donationNo);
        }
        return donation;
    }

    /**
     * 根据捐赠ID查询捐赠记录（包含物资明细）
     */
    public MaterialDonation getDonationWithItems(Long donationId) {
        MaterialDonation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new IllegalArgumentException("捐赠记录不存在：" + donationId));
        // 可以在这里加载物资明细
        return donation;
    }

    /**
     * 根据捐赠者ID查询捐赠记录
     */
    public List<MaterialDonation> getByDonorId(Long donorId) {
        return donationRepository.findByDonorId(donorId);
    }

    /**
     * 修复数据库中的枚举值（将小写转换为大写）
     */
    @Transactional
    public int fixEnumValues() {
        int count = 0;

        // 1. 首先将 ENUM 列改为 VARCHAR
        try {
            // material_donation 表
            entityManager.createNativeQuery(
                    "ALTER TABLE material_donation " +
                            "MODIFY COLUMN donation_method VARCHAR(20) NOT NULL DEFAULT 'MAIL' " +
                            "COMMENT '捐赠方式：MAIL-邮寄，PICKUP-上门取件'"
            ).executeUpdate();

            entityManager.createNativeQuery(
                    "ALTER TABLE material_donation " +
                            "MODIFY COLUMN status VARCHAR(20) NOT NULL DEFAULT 'CREATED' " +
                            "COMMENT '状态'"
            ).executeUpdate();

            // material_item 表
            entityManager.createNativeQuery(
                    "ALTER TABLE material_item " +
                            "MODIFY COLUMN condition_level VARCHAR(20) NOT NULL DEFAULT 'GOOD' " +
                            "COMMENT '新旧程度'"
            ).executeUpdate();

            entityManager.createNativeQuery(
                    "ALTER TABLE material_item " +
                            "MODIFY COLUMN inspection_result VARCHAR(20) DEFAULT 'PENDING' " +
                            "COMMENT '检验结果'"
            ).executeUpdate();

            log.info("已将 ENUM 列改为 VARCHAR 类型");
        } catch (Exception e) {
            log.warn("修改列类型失败（可能已经是VARCHAR）: {}", e.getMessage());
        }

        // 2. 然后更新数据
        // 使用原生 SQL 更新 donation_method 字段 - 分别更新
        int mailUpdated = entityManager.createNativeQuery(
                "UPDATE material_donation SET donation_method = 'MAIL' WHERE LOWER(donation_method) = 'mail'"
        ).executeUpdate();
        count += mailUpdated;

        int pickupUpdated = entityManager.createNativeQuery(
                "UPDATE material_donation SET donation_method = 'PICKUP' WHERE LOWER(donation_method) = 'pickup'"
        ).executeUpdate();
        count += pickupUpdated;

        // 修复 status 字段 - 分别更新每个状态
        int createdUpdated = entityManager.createNativeQuery(
                "UPDATE material_donation SET status = 'CREATED' WHERE LOWER(status) = 'created'"
        ).executeUpdate();
        count += createdUpdated;

        int receivedUpdated = entityManager.createNativeQuery(
                "UPDATE material_donation SET status = 'RECEIVED' WHERE LOWER(status) = 'received'"
        ).executeUpdate();
        count += receivedUpdated;

        // 修复 material_item 表的 condition_level 字段
        int newUpdated = entityManager.createNativeQuery(
                "UPDATE material_item SET condition_level = 'NEW' WHERE LOWER(condition_level) = 'new'"
        ).executeUpdate();
        count += newUpdated;

        int likeNewUpdated = entityManager.createNativeQuery(
                "UPDATE material_item SET condition_level = 'LIKE_NEW' WHERE LOWER(condition_level) = 'like_new'"
        ).executeUpdate();
        count += likeNewUpdated;

        int goodUpdated = entityManager.createNativeQuery(
                "UPDATE material_item SET condition_level = 'GOOD' WHERE LOWER(condition_level) = 'good'"
        ).executeUpdate();
        count += goodUpdated;

        // 修复 material_item 表的 inspection_result 字段
        int pendingUpdated = entityManager.createNativeQuery(
                "UPDATE material_item SET inspection_result = 'PENDING' WHERE LOWER(inspection_result) = 'pending'"
        ).executeUpdate();
        count += pendingUpdated;

        int qualifiedUpdated = entityManager.createNativeQuery(
                "UPDATE material_item SET inspection_result = 'QUALIFIED' WHERE LOWER(inspection_result) = 'qualified'"
        ).executeUpdate();
        count += qualifiedUpdated;

        log.info("修复了 {} 条记录的枚举值（method: mail={}, pickup={}, status: created={}, received={}, " +
                        "condition: new={}, like_new={}, good={}, inspection: pending={}, qualified={}）",
                count, mailUpdated, pickupUpdated, createdUpdated, receivedUpdated,
                newUpdated, likeNewUpdated, goodUpdated, pendingUpdated, qualifiedUpdated);
        return count;
    }

    /**
     * 调试：查询枚举值情况
     */
    public String debugEnumValues() {
        StringBuilder sb = new StringBuilder();

        // 查询 material_donation 表
        @SuppressWarnings("unchecked")
        List<Object[]> donationResults = entityManager.createNativeQuery(
                "SELECT id, donation_no, donation_method, status FROM material_donation ORDER BY id LIMIT 20"
        ).getResultList();

        sb.append("=== material_donation 表（前20条）===\n");
        for (Object[] row : donationResults) {
            sb.append(String.format("ID: %s, No: %s, Method: %s, Status: %s\n",
                    row[0], row[1], row[2], row[3]));
        }

        // 查询 material_item 表
        @SuppressWarnings("unchecked")
        List<Object[]> itemResults = entityManager.createNativeQuery(
                "SELECT id, donation_id, condition_level, inspection_result FROM material_item ORDER BY id LIMIT 10"
        ).getResultList();

        sb.append("\n=== material_item 表（前10条）===\n");
        for (Object[] row : itemResults) {
            sb.append(String.format("ID: %s, DonationID: %s, Condition: %s, Inspection: %s\n",
                    row[0], row[1], row[2], row[3]));
        }

        return sb.toString();
    }

    /**
     * 获取表结构
     */
    public String getTableStructure() {
        StringBuilder sb = new StringBuilder();

        // 获取 material_donation 表的创建语句
        @SuppressWarnings("unchecked")
        List<Object[]> donationTable = entityManager.createNativeQuery(
                "SHOW CREATE TABLE material_donation"
        ).getResultList();

        sb.append("=== material_donation 表结构 ===\n");
        for (Object[] row : donationTable) {
            if (row.length > 1) {
                sb.append(row[1]).append("\n");
            }
        }

        // 获取 material_item 表的创建语句
        @SuppressWarnings("unchecked")
        List<Object[]> itemTable = entityManager.createNativeQuery(
                "SHOW CREATE TABLE material_item"
        ).getResultList();

        sb.append("\n=== material_item 表结构 ===\n");
        for (Object[] row : itemTable) {
            if (row.length > 1) {
                sb.append(row[1]).append("\n");
            }
        }

        return sb.toString();
    }
}
