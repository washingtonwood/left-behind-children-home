package com.shouhutongxing.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 进度附件实体（照片/视频）
 *
 * @author washingtonwood
 * @since 2025-01-25
 */
@Data
@Entity
@Table(name = "progress_attachment")
public class ProgressAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 进度ID（关联help_progress表）
     */
    @Column(name = "progress_id", nullable = false)
    private Long progressId;

    /**
     * 附件类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "attachment_type", nullable = false, length = 20)
    private AttachmentType attachmentType;

    /**
     * 文件名
     */
    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    /**
     * 文件URL
     */
    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    /**
     * 文件大小（字节）
     */
    @Column(name = "file_size")
    private Long fileSize;

    /**
     * MIME类型
     */
    @Column(name = "mime_type", length = 100)
    private String mimeType;

    /**
     * 照片描述（仅photo类型）
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 是否为封面图
     */
    @Column(name = "is_cover", nullable = false)
    private Boolean isCover = false;

    /**
     * 显示顺序
     */
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;

    /**
     * 是否已做隐私处理
     */
    @Column(name = "privacy_processed", nullable = false)
    private Boolean privacyProcessed = false;

    /**
     * 创建时间
     */
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    /**
     * 附件类型枚举
     */
    public enum AttachmentType {
        PHOTO,     // 照片
        VIDEO,     // 视频
        DOCUMENT   // 文档
    }

    /**
     * JPA 生命周期回调
     */
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }

    /**
     * 判断是否为照片
     */
    public boolean isPhoto() {
        return attachmentType == AttachmentType.PHOTO;
    }

    /**
     * 判断是否为视频
     */
    public boolean isVideo() {
        return attachmentType == AttachmentType.VIDEO;
    }

    /**
     * 获取文件扩展名
     */
    public String getFileExtension() {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 判断文件大小是否超过限制（50MB）
     */
    public boolean isOverSize() {
        final long MAX_SIZE = 50 * 1024 * 1024; // 50MB
        return fileSize != null && fileSize > MAX_SIZE;
    }

    /**
     * 格式化文件大小显示
     */
    public String getFormattedFileSize() {
        if (fileSize == null) {
            return "未知大小";
        }
        if (fileSize < 1024) {
            return fileSize + " B";
        } else if (fileSize < 1024 * 1024) {
            return String.format("%.2f KB", fileSize / 1024.0);
        } else if (fileSize < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", fileSize / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", fileSize / (1024.0 * 1024 * 1024));
        }
    }
}
