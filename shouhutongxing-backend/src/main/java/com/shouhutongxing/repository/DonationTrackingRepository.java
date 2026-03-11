package com.shouhutongxing.repository;

import com.shouhutongxing.entity.DonationTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 捐赠追踪 Repository
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Repository
public interface DonationTrackingRepository extends JpaRepository<DonationTracking, Long> {

    /**
     * 根据捐赠ID查询所有追踪记录
     */
    List<DonationTracking> findByDonationIdOrderByCreatedTimeAsc(Long donationId);

    /**
     * 根据捐赠ID和节点类型查询
     */
    List<DonationTracking> findByDonationIdAndNodeType(Long donationId, String nodeType);

    /**
     * 查询指定捐赠的最新追踪记录
     */
    List<DonationTracking> findTop1ByDonationIdOrderByCreatedTimeDesc(Long donationId);

    /**
     * 根据操作员查询追踪记录
     */
    List<DonationTracking> findByOperatorIdOrderByCreatedTimeDesc(Long operatorId);

    /**
     * 查询指定时间范围内的追踪记录
     */
    List<DonationTracking> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计指定捐赠的追踪节点数量
     */
    long countByDonationId(Long donationId);

    /**
     * 查询有照片的追踪记录
     */
    @Query("SELECT t FROM DonationTracking t WHERE t.donationId = :donationId AND t.photoUrls IS NOT NULL AND t.photoUrls != ''")
    List<DonationTracking> findWithPhotosByDonationId(@Param("donationId") Long donationId);

    /**
     * 查询有关键节点的追踪记录（有照片或视频）
     */
    @Query("SELECT t FROM DonationTracking t WHERE t.photoUrls IS NOT NULL OR t.videoUrl IS NOT NULL ORDER BY t.createdTime DESC")
    List<DonationTracking> findWithMedia();

    /**
     * 查询最新的追踪记录（所有捐赠）
     */
    List<DonationTracking> findTop20ByOrderByCreatedTimeDesc();

    /**
     * 根据节点类型查询
     */
    List<DonationTracking> findByNodeTypeOrderByCreatedTimeDesc(String nodeType);

    /**
     * 查询指定捐赠的当前状态节点
     */
    @Query("SELECT t FROM DonationTracking t WHERE t.donationId = :donationId ORDER BY t.createdTime DESC LIMIT 1")
    DonationTracking findLatestTrackingByDonationId(@Param("donationId") Long donationId);
}
