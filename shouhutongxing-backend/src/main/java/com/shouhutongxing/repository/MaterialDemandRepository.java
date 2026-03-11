package com.shouhutongxing.repository;

import com.shouhutongxing.entity.MaterialDemand;
import com.shouhutongxing.entity.MaterialDemand.DemandStatus;
import com.shouhutongxing.entity.MaterialDemand.UrgencyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 物资需求 Repository
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Repository
public interface MaterialDemandRepository extends JpaRepository<MaterialDemand, Long> {

    /**
     * 根据儿童ID查询需求
     */
    List<MaterialDemand> findByChildId(Long childId);

    /**
     * 根据状态查询需求
     */
    List<MaterialDemand> findByStatus(DemandStatus status);

    /**
     * 根据紧急程度查询
     */
    List<MaterialDemand> findByUrgencyLevel(UrgencyLevel urgencyLevel);

    /**
     * 根据物资分类查询
     */
    List<MaterialDemand> findByCategory(String category);

    /**
     * 查询紧急需求（状态为活跃且紧急程度为urgent）
     */
    @Query("SELECT d FROM MaterialDemand d WHERE d.status = 'ACTIVE' AND d.urgencyLevel = 'URGENT' ORDER BY d.createdTime ASC")
    List<MaterialDemand> findUrgentDemands();

    /**
     * 查询未满足的需求（receivedQuantity < requiredQuantity）
     */
    @Query("SELECT d FROM MaterialDemand d WHERE d.status = 'ACTIVE' AND d.receivedQuantity < d.requiredQuantity ORDER BY d.urgencyLevel DESC, d.createdTime ASC")
    List<MaterialDemand> findUnfulfilledDemands();

    /**
     * 根据儿童ID和状态查询
     */
    List<MaterialDemand> findByChildIdAndStatus(Long childId, DemandStatus status);

    /**
     * 统计指定儿童的需求数量
     */
    long countByChildId(Long childId);

    /**
     * 统计指定分类的需求总数
     */
    @Query("SELECT SUM(d.requiredQuantity) FROM MaterialDemand d WHERE d.category = :category")
    Long sumRequiredQuantityByCategory(@Param("category") String category);

    /**
     * 统计指定分类的已接收数量
     */
    @Query("SELECT SUM(d.receivedQuantity) FROM MaterialDemand d WHERE d.category = :category")
    Long sumReceivedQuantityByCategory(@Param("category") String category);

    /**
     * 查询即将到期的需求（7天内）
     */
    @Query("SELECT d FROM MaterialDemand d WHERE d.status = 'ACTIVE' AND d.deadline <= :deadline ORDER BY d.deadline ASC")
    List<MaterialDemand> findUpcomingDeadlines(@Param("deadline") java.time.LocalDate deadline);

    /**
     * 查询已完成的需求
     */
    @Query("SELECT d FROM MaterialDemand d WHERE d.receivedQuantity >= d.requiredQuantity")
    List<MaterialDemand> findFulfilledDemands();
}
