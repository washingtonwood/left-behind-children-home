package com.shouhutongxing.service;

import com.shouhutongxing.dto.DonorRank;
import com.shouhutongxing.dto.FundDonationRequest;
import com.shouhutongxing.dto.FundDonationResponse;
import com.shouhutongxing.entity.FundDonation;
import com.shouhutongxing.repository.FundDonationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资金捐赠服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FundDonationService {

    private final FundDonationRepository fundDonationRepository;

    /**
     * 创建捐赠记录
     */
    @Transactional
    public FundDonationResponse createDonation(FundDonationRequest request) {
        log.info("创建资金捐赠记录: {}", request);

        FundDonation donation = new FundDonation();
        donation.setProjectType(FundDonation.ProjectType.valueOf(request.getProjectType()));
        donation.setAmount(request.getAmount().doubleValue());
        donation.setDonorName(request.getDonorName());
        donation.setDonorPhone(request.getDonorPhone());
        donation.setDonorEmail(request.getDonorEmail());
        donation.setPaymentMethod(FundDonation.PaymentMethod.valueOf(request.getPaymentMethod()));
        if (request.getRecurringType() != null) {
            donation.setRecurringType(FundDonation.RecurringType.valueOf(request.getRecurringType()));
        } else {
            donation.setRecurringType(FundDonation.RecurringType.NONE);
        }
        donation.setMessage(request.getMessage());
        donation.setIsAnonymous(request.getIsAnonymous() != null ? request.getIsAnonymous() : false);
        donation.setStatus(FundDonation.DonationStatus.SUCCESS); // 前端模拟直接成功
        donation.setPaidAt(LocalDateTime.now());

        FundDonation saved = fundDonationRepository.save(donation);

        return buildResponse(saved);
    }

    /**
     * 获取项目统计数据
     */
    public Map<String, FundDonationResponse.ProjectStatistics> getProjectStatistics() {
        Map<String, FundDonationResponse.ProjectStatistics> statistics = new HashMap<>();

        for (FundDonation.ProjectType projectType : FundDonation.ProjectType.values()) {
            BigDecimal raisedAmount = fundDonationRepository.sumAmountByProjectType(projectType);
            Long donorCount = fundDonationRepository.countDonorsByProjectType(projectType);
            BigDecimal targetAmount = getTargetAmount(projectType);

            FundDonationResponse.ProjectStatistics stat = new FundDonationResponse.ProjectStatistics();
            stat.setProjectType(projectType.name());
            stat.setRaisedAmount(raisedAmount != null ? raisedAmount : BigDecimal.ZERO);
            stat.setTargetAmount(targetAmount);
            stat.setDonorCount(donorCount != null ? donorCount.intValue() : 0);

            int percentage = targetAmount.compareTo(BigDecimal.ZERO) > 0
                ? stat.getRaisedAmount().multiply(BigDecimal.valueOf(100))
                    .divide(targetAmount, 0, java.math.RoundingMode.HALF_UP).intValue()
                : 0;
            stat.setPercentage(Math.min(percentage, 100));

            statistics.put(projectType.name(), stat);
        }

        return statistics;
    }

    /**
     * 获取本月排行榜
     */
    public List<Map<String, Object>> getMonthlyLeaderboard() {
        LocalDateTime startOfMonth = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth());
        List<DonorRank> rankings = fundDonationRepository.findMonthlyLeaderboard(startOfMonth);

        return rankings.stream()
            .limit(5)
            .map(rank -> {
                Map<String, Object> item = new HashMap<>();
                item.put("donorName", rank.getDonorName());
                item.put("totalAmount", rank.getTotalAmount());
                item.put("donationCount", 1); // 简化处理
                return item;
            })
            .toList();
    }

    /**
     * 获取累计排行榜
     */
    public List<Map<String, Object>> getCumulativeLeaderboard() {
        List<DonorRank> rankings = fundDonationRepository.findCumulativeLeaderboard();

        return rankings.stream()
            .limit(5)
            .map(rank -> {
                Map<String, Object> item = new HashMap<>();
                item.put("donorName", rank.getDonorName());
                item.put("totalAmount", rank.getTotalAmount());
                item.put("donationCount", 1); // 简化处理
                return item;
            })
            .toList();
    }

    /**
     * 获取感恩墙数据
     */
    public List<Map<String, Object>> getGratitudeWall() {
        List<FundDonation> donations = fundDonationRepository.findRecentDonationsForGratitude();

        return donations.stream()
            .limit(6)
            .map(donation -> {
                Map<String, Object> item = new HashMap<>();
                item.put("donorName", donation.getDonorName());
                item.put("amount", BigDecimal.valueOf(donation.getAmount()));
                item.put("message", donation.getMessage());
                item.put("projectType", donation.getProjectType().name());
                item.put("createdAt", donation.getPaidAt());
                return item;
            })
            .toList();
    }

    private FundDonationResponse buildResponse(FundDonation donation) {
        FundDonationResponse response = new FundDonationResponse();
        response.setId(donation.getId());
        response.setDonationNo(donation.getDonationNo());
        response.setProjectType(donation.getProjectType().name());
        response.setProjectName(donation.getProjectType().getDescription());
        response.setAmount(BigDecimal.valueOf(donation.getAmount()));
        response.setDonorName(donation.getDonorName());
        response.setCreatedAt(donation.getCreatedAt());

        // 添加项目统计
        FundDonationResponse.ProjectStatistics statistics = new FundDonationResponse.ProjectStatistics();
        statistics.setProjectType(donation.getProjectType().name());

        BigDecimal raisedAmount = fundDonationRepository.sumAmountByProjectType(donation.getProjectType());
        statistics.setRaisedAmount(raisedAmount != null ? raisedAmount : BigDecimal.ZERO);

        BigDecimal targetAmount = getTargetAmount(donation.getProjectType());
        statistics.setTargetAmount(targetAmount);

        Long donorCount = fundDonationRepository.countDonorsByProjectType(donation.getProjectType());
        statistics.setDonorCount(donorCount != null ? donorCount.intValue() : 0);

        int percentage = targetAmount.compareTo(BigDecimal.ZERO) > 0
            ? statistics.getRaisedAmount().multiply(BigDecimal.valueOf(100))
                .divide(targetAmount, 0, java.math.RoundingMode.HALF_UP).intValue()
            : 0;
        statistics.setPercentage(Math.min(percentage, 100));

        response.setProjectStatistics(statistics);

        return response;
    }

    private BigDecimal getTargetAmount(FundDonation.ProjectType projectType) {
        switch (projectType) {
            case EDUCATION:
                return BigDecimal.valueOf(500000);
            case MEDICAL:
                return BigDecimal.valueOf(300000);
            case LIVING:
                return BigDecimal.valueOf(200000);
            default:
                return BigDecimal.ZERO;
        }
    }
}
