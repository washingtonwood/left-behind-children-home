package com.shouhutongxing.controller;

import com.shouhutongxing.dto.FundDonationRequest;
import com.shouhutongxing.dto.FundDonationResponse;
import com.shouhutongxing.dto.Result;
import com.shouhutongxing.service.FundDonationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 资金捐赠控制器
 */
@Slf4j
@RestController
@RequestMapping("/fund-donations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class FundDonationController {

    private final FundDonationService fundDonationService;

    /**
     * 创建捐赠
     */
    @PostMapping
    public Result<FundDonationResponse> createDonation(@Valid @RequestBody FundDonationRequest request) {
        log.info("收到资金捐赠请求: {}", request);
        FundDonationResponse response = fundDonationService.createDonation(request);
        return Result.success(response);
    }

    /**
     * 获取项目统计数据
     */
    @GetMapping("/statistics")
    public Result<Map<String, FundDonationResponse.ProjectStatistics>> getProjectStatistics() {
        Map<String, FundDonationResponse.ProjectStatistics> statistics = fundDonationService.getProjectStatistics();
        return Result.success(statistics);
    }

    /**
     * 获取本月排行榜
     */
    @GetMapping("/leaderboard/monthly")
    public Result<List<Map<String, Object>>> getMonthlyLeaderboard() {
        List<Map<String, Object>> leaderboard = fundDonationService.getMonthlyLeaderboard();
        return Result.success(leaderboard);
    }

    /**
     * 获取累计排行榜
     */
    @GetMapping("/leaderboard/cumulative")
    public Result<List<Map<String, Object>>> getCumulativeLeaderboard() {
        List<Map<String, Object>> leaderboard = fundDonationService.getCumulativeLeaderboard();
        return Result.success(leaderboard);
    }

    /**
     * 获取感恩墙数据
     */
    @GetMapping("/gratitude-wall")
    public Result<List<Map<String, Object>>> getGratitudeWall() {
        List<Map<String, Object>> gratitudeWall = fundDonationService.getGratitudeWall();
        return Result.success(gratitudeWall);
    }
}
