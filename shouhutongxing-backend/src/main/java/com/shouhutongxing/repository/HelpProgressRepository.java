package com.shouhutongxing.repository;

import com.shouhutongxing.entity.HelpProgress;
import com.shouhutongxing.entity.HelpProgress.ProgressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 帮扶进度 Repository
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Repository
public interface HelpProgressRepository extends JpaRepository<HelpProgress, Long> {

    /**
     * 根据儿童ID查询进度记录
     */
    List<HelpProgress> findByChildIdOrderByActivityDateDesc(Long childId);

    /**
     * 根据状态查询
     */
    List<HelpProgress> findByStatusOrderByCreatedTimeDesc(ProgressStatus status);

    /**
     * 根据活动类型查询
     */
    List<HelpProgress> findByActivityTypeOrderByActivityDateDesc(String activityType);

    /**
     * 根据项目ID查询进度记录
     */
    List<HelpProgress> findByProjectIdOrderByActivityDateDesc(Long projectId);

    /**
     * 根据捐赠ID查询进度记录
     */
    List<HelpProgress> findByDonationId(Long donationId);

    /**
     * 查询已发布的进度（公开可见）
     */
    @Query("SELECT h FROM HelpProgress h WHERE h.status = 'PUBLISHED' ORDER BY h.activityDate DESC")
    List<HelpProgress> findPublishedProgress();

    /**
     * 查询待审核的进度
     */
    List<HelpProgress> findByStatus(ProgressStatus status);

    /**
     * 根据儿童ID和状态查询
     */
    List<HelpProgress> findByChildIdAndStatusOrderByActivityDateDesc(Long childId, ProgressStatus status);

    /**
     * 统计指定儿童的进度数量
     */
    long countByChildId(Long childId);

    /**
     * 统计指定活动类型的数量
     */
    long countByActivityType(String activityType);

    /**
     * 查询指定日期范围内的进度
     */
    List<HelpProgress> findByActivityDateBetweenOrderByActivityDateAsc(LocalDate startDate, LocalDate endDate);

    /**
     * 查询最近发布的进度（前20条）
     */
    @Query("SELECT h FROM HelpProgress h WHERE h.status = 'PUBLISHED' ORDER BY h.publishedTime DESC")
    List<HelpProgress> findRecentPublishedProgress();

    /**
     * 查询指定儿童的最新进度
     */
    List<HelpProgress> findTop5ByChildIdAndStatusOrderByActivityDateDesc(Long childId, ProgressStatus status);

    /**
     * 统计各活动类型的数量
     */
    @Query("SELECT h.activityType, COUNT(h) FROM HelpProgress h WHERE h.status = 'PUBLISHED' GROUP BY h.activityType")
    List<Object[]> countByActivityType();

    /**
     * 查询有关联捐赠的进度
     */
    @Query("SELECT h FROM HelpProgress h WHERE h.donationId IS NOT NULL AND h.status = 'PUBLISHED' ORDER BY h.activityDate DESC")
    List<HelpProgress> findProgressWithDonation();

    /**
     * 查询草稿状态的进度
     */
    List<HelpProgress> findByStatusOrderByActivityDateDesc(ProgressStatus status);

    /**
     * 根据审核人ID查询已审核的进度
     */
    List<HelpProgress> findByReviewerIdOrderByReviewTimeDesc(Long reviewerId);

    /**
     * 统计已发布的进度总数
     */
    long countByStatus(ProgressStatus status);

    /**
     * 查询指定时间范围内创建的进度
     */
    List<HelpProgress> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询包含关键词的进度（标题或描述）
     */
    @Query("SELECT h FROM HelpProgress h WHERE h.status = 'PUBLISHED' AND (h.activityTitle LIKE %:keyword% OR h.activityDescription LIKE %:keyword%) ORDER BY h.activityDate DESC")
    List<HelpProgress> searchPublishedProgress(@Param("keyword") String keyword);
}
