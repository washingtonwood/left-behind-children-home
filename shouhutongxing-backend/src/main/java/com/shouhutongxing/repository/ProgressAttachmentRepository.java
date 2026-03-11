package com.shouhutongxing.repository;

import com.shouhutongxing.entity.ProgressAttachment;
import com.shouhutongxing.entity.ProgressAttachment.AttachmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 进度附件 Repository
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Repository
public interface ProgressAttachmentRepository extends JpaRepository<ProgressAttachment, Long> {

    /**
     * 根据进度ID查询所有附件
     */
    List<ProgressAttachment> findByProgressIdOrderByDisplayOrderAsc(Long progressId);

    /**
     * 根据进度ID和附件类型查询
     */
    List<ProgressAttachment> findByProgressIdAndAttachmentTypeOrderByDisplayOrderAsc(Long progressId, AttachmentType attachmentType);

    /**
     * 查询进度ID的封面图
     */
    List<ProgressAttachment> findByProgressIdAndIsCoverTrueOrderByDisplayOrderAsc(Long progressId);

    /**
     * 查询进度ID的第一张封面图
     */
    ProgressAttachment findFirstByProgressIdAndIsCoverTrueOrderByDisplayOrderAsc(Long progressId);

    /**
     * 根据附件类型查询
     */
    List<ProgressAttachment> findByAttachmentTypeOrderByCreatedTimeDesc(AttachmentType attachmentType);

    /**
     * 统计指定进度的附件数量
     */
    long countByProgressId(Long progressId);

    /**
     * 统计指定进度指定类型的附件数量
     */
    long countByProgressIdAndAttachmentType(Long progressId, AttachmentType attachmentType);

    /**
     * 查询未做隐私处理的附件
     */
    @Query("SELECT a FROM ProgressAttachment a WHERE a.attachmentType = 'PHOTO' AND a.privacyProcessed = false")
    List<ProgressAttachment> findPhotosWithoutPrivacy();

    /**
     * 查询大文件（>10MB）
     */
    @Query("SELECT a FROM ProgressAttachment a WHERE a.fileSize > 10485760 ORDER BY a.fileSize DESC")
    List<ProgressAttachment> findLargeFiles();

    /**
     * 统计各类型附件的数量
     */
    @Query("SELECT a.attachmentType, COUNT(a) FROM ProgressAttachment a GROUP BY a.attachmentType")
    List<Object[]> countByAttachmentType();

    /**
     * 查询指定进度的照片
     */
    @Query("SELECT a FROM ProgressAttachment a WHERE a.progressId = :progressId AND a.attachmentType = 'PHOTO' ORDER BY a.displayOrder ASC")
    List<ProgressAttachment> findPhotosByProgressId(@Param("progressId") Long progressId);

    /**
     * 查询指定进度的视频
     */
    @Query("SELECT a FROM ProgressAttachment a WHERE a.progressId = :progressId AND a.attachmentType = 'VIDEO' ORDER BY a.displayOrder ASC")
    List<ProgressAttachment> findVideosByProgressId(@Param("progressId") Long progressId);

    /**
     * 查询所有封面图
     */
    List<ProgressAttachment> findByIsCoverTrueOrderByCreatedTimeDesc();

    /**
     * 查询最近上传的附件
     */
    List<ProgressAttachment> findTop20ByOrderByCreatedTimeDesc();

    /**
     * 根据文件名查询（检查重复）
     */
    List<ProgressAttachment> findByFileName(String fileName);

    /**
     * 计算指定进度的附件总大小
     */
    @Query("SELECT SUM(a.fileSize) FROM ProgressAttachment a WHERE a.progressId = :progressId")
    Long sumFileSizeByProgressId(@Param("progressId") Long progressId);

    /**
     * 查询指定进地的附件总数
     */
    List<ProgressAttachment> findByProgressId(Long progressId);
}
