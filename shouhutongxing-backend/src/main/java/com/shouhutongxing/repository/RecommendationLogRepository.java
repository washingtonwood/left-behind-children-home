package com.shouhutongxing.repository;

import com.shouhutongxing.entity.RecommendationLog;
import com.shouhutongxing.entity.RecommendationLog.ActionTaken;
import com.shouhutongxing.entity.RecommendationLog.SourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 推荐日志 Repository
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Repository
public interface RecommendationLogRepository extends JpaRepository<RecommendationLog, Long> {

    /**
     * 根据儿童ID查询推荐记录
     */
    List<RecommendationLog> findByChildIdOrderByCreatedTimeDesc(Long childId);

    /**
     * 根据捐赠者ID查询推荐记录
     */
    List<RecommendationLog> findByDonorIdOrderByCreatedTimeDesc(Long donorId);

    /**
     * 根据推荐来源查询
     */
    List<RecommendationLog> findBySourceTypeOrderByCreatedTimeDesc(SourceType sourceType);

    /**
     * 根据用户操作查询
     */
    List<RecommendationLog> findByActionTakenOrderByActionTimeDesc(ActionTaken actionTaken);

    /**
     * 查询已转化的推荐（用户已捐赠）
     */
    List<RecommendationLog> findByActionTaken(ActionTaken actionTaken);

    /**
     * 统计指定儿童的推荐次数
     */
    long countByChildId(Long childId);

    /**
     * 统计指定捐赠者的推荐次数
     */
    long countByDonorId(Long donorId);

    /**
     * 统计转化率（已捐赠/总数）
     */
    @Query("SELECT COUNT(r) FROM RecommendationLog r WHERE r.actionTaken = 'DONATED'")
    long countConverted();

    /**
     * 查询高推荐得分（>=85）
     */
    @Query("SELECT r FROM RecommendationLog r WHERE r.score >= 85 ORDER BY r.score DESC")
    List<RecommendationLog> findHighScoreRecommendations();

    /**
     * 查询指定时间范围内的推荐记录
     */
    List<RecommendationLog> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据儿童和捐赠者查询推荐记录
     */
    List<RecommendationLog> findByChildIdAndDonorIdOrderByCreatedTimeDesc(Long childId, Long donorId);

    /**
     * 查询未查看的推荐（action_taken = 'viewed' 且 action_time IS NULL）
     */
    @Query("SELECT r FROM RecommendationLog r WHERE r.actionTaken = 'VIEWED' AND r.actionTime IS NULL")
    List<RecommendationLog> findUnviewedRecommendations();

    /**
     * 统计各来源的推荐数量
     */
    @Query("SELECT r.sourceType, COUNT(r) FROM RecommendationLog r GROUP BY r.sourceType")
    List<Object[]> countBySourceType();

    /**
     * 查询推荐效果（儿童ID -> 平均得分）
     */
    @Query("SELECT r.childId, AVG(r.score) FROM RecommendationLog r GROUP BY r.childId ORDER BY AVG(r.score) DESC")
    List<Object[]> findAverageScoreByChild();

    /**
     * 查询最近的推荐记录（前100条）
     */
    List<RecommendationLog> findTop100ByOrderByCreatedTimeDesc();
}
