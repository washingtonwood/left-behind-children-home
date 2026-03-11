package com.shouhutongxing.repository;

import com.shouhutongxing.entity.MaterialDonation;
import com.shouhutongxing.entity.MaterialDonation.DonationMethod;
import com.shouhutongxing.entity.MaterialDonation.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物资捐赠 Repository
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Repository
public interface MaterialDonationRepository extends JpaRepository<MaterialDonation, Long> {

    /**
     * 根据捐赠者ID查询捐赠记录
     */
    List<MaterialDonation> findByDonorId(Long donorId);

    /**
     * 根据捐赠编号查询
     */
    MaterialDonation findByDonationNo(String donationNo);

    /**
     * 根据状态查询捐赠记录
     */
    List<MaterialDonation> findByStatus(DonationStatus status);

    /**
     * 根据捐赠方式查询
     */
    List<MaterialDonation> findByDonationMethod(DonationMethod donationMethod);

    /**
     * 查询指定时间范围内的捐赠记录
     */
    List<MaterialDonation> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据捐赠者ID和状态查询
     */
    List<MaterialDonation> findByDonorIdAndStatus(Long donorId, DonationStatus status);

    /**
     * 查询最近的捐赠记录（按创建时间倒序）
     */
    List<MaterialDonation> findTop10ByOrderByCreatedTimeDesc();

    /**
     * 统计指定状态的捐赠数量
     */
    long countByStatus(DonationStatus status);

    /**
     * 统计指定捐赠者的捐赠次数
     */
    long countByDonorId(Long donorId);

    /**
     * 查询待处理的捐赠（已创建、已发货、已接收）
     */
    @Query("SELECT d FROM MaterialDonation d WHERE d.status IN ('CREATED', 'SHIPPED', 'RECEIVED') ORDER BY d.createdTime ASC")
    List<MaterialDonation> findPendingDonations();

    /**
     * 查询已完成的捐赠（最近30天）
     */
    @Query("SELECT d FROM MaterialDonation d WHERE d.status = 'COMPLETED' AND d.createdTime >= :startTime")
    List<MaterialDonation> findRecentlyCompleted(@Param("startTime") LocalDateTime startTime);

    /**
     * 统计总捐赠件数
     */
    @Query("SELECT SUM(d.totalItems) FROM MaterialDonation d")
    Long sumTotalItems();

    /**
     * 统计总估算价值
     */
    @Query("SELECT SUM(d.estimatedValue) FROM MaterialDonation d")
    Long sumEstimatedValue();
}
