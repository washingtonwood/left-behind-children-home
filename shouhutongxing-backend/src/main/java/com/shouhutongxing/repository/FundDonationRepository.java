package com.shouhutongxing.repository;

import com.shouhutongxing.dto.DonorRank;
import com.shouhutongxing.entity.FundDonation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 资金捐赠Repository
 */
@Repository
public interface FundDonationRepository extends JpaRepository<FundDonation, Long> {

    /**
     * 根据捐赠编号查询
     */
    Optional<FundDonation> findByDonationNo(String donationNo);

    /**
     * 统计指定项目的已筹集金额
     */
    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM FundDonation d WHERE d.projectType = :projectType AND d.status = 'SUCCESS'")
    BigDecimal sumAmountByProjectType(@Param("projectType") FundDonation.ProjectType projectType);

    /**
     * 统计指定项目的捐赠人数
     */
    @Query("SELECT COUNT(DISTINCT d.donorPhone) FROM FundDonation d WHERE d.projectType = :projectType AND d.status = 'SUCCESS'")
    Long countDonorsByProjectType(@Param("projectType") FundDonation.ProjectType projectType);

    /**
     * 查询本月捐赠排行榜
     */
    @Query("SELECT new com.shouhutongxing.dto.DonorRank(d.donorName, CAST(SUM(d.amount) AS bigdecimal)) " +
           "FROM FundDonation d " +
           "WHERE d.status = 'SUCCESS' AND d.isAnonymous = false " +
           "AND d.paidAt >= :startOfMonth " +
           "GROUP BY d.donorPhone, d.donorName " +
           "ORDER BY SUM(d.amount) DESC")
    List<DonorRank> findMonthlyLeaderboard(@Param("startOfMonth") LocalDateTime startOfMonth);

    /**
     * 查询累计捐赠排行榜
     */
    @Query("SELECT new com.shouhutongxing.dto.DonorRank(d.donorName, CAST(SUM(d.amount) AS bigdecimal)) " +
           "FROM FundDonation d " +
           "WHERE d.status = 'SUCCESS' AND d.isAnonymous = false " +
           "GROUP BY d.donorPhone, d.donorName " +
           "ORDER BY SUM(d.amount) DESC")
    List<DonorRank> findCumulativeLeaderboard();

    /**
     * 查询最近的捐赠记录（用于感恩墙）
     */
    @Query("SELECT d FROM FundDonation d " +
           "WHERE d.status = 'SUCCESS' AND d.isAnonymous = false AND d.message IS NOT NULL " +
           "ORDER BY d.paidAt DESC")
    List<FundDonation> findRecentDonationsForGratitude();
}
