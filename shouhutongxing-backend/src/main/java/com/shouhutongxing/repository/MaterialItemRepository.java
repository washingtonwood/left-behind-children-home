package com.shouhutongxing.repository;

import com.shouhutongxing.entity.MaterialItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 物资明细 Repository
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Repository
public interface MaterialItemRepository extends JpaRepository<MaterialItem, Long> {

    /**
     * 根据捐赠ID查询物资明细
     */
    List<MaterialItem> findByDonationId(Long donationId);

    /**
     * 根据需求ID查询物资明细
     */
    List<MaterialItem> findByDemandId(Long demandId);

    /**
     * 根据捐赠ID删除物资明细
     */
    void deleteByDonationId(Long donationId);
}
