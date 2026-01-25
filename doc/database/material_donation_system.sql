-- =====================================================
-- 物资捐赠与追踪系统数据库表结构
-- 创建时间: 2025-01-25
-- 说明: 包含物资捐赠、推荐、追踪、进度公开等 7 个新表
-- =====================================================

-- 使用现有数据库
USE school;

-- =====================================================
-- 1. 物资捐赠记录主表
-- =====================================================
CREATE TABLE material_donation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '捐赠ID',
    donation_no VARCHAR(50) NOT NULL UNIQUE COMMENT '捐赠编号（MD202501250001）',
    donor_id BIGINT COMMENT '捐赠者ID（关联user表，NULL表示匿名捐赠）',
    donor_name VARCHAR(100) COMMENT '捐赠者姓名',
    donor_phone VARCHAR(20) COMMENT '捐赠者电话',
    donor_email VARCHAR(100) COMMENT '捐赠者邮箱',

    -- 捐赠方式
    donation_method ENUM('mail', 'pickup') NOT NULL DEFAULT 'mail' COMMENT '捐赠方式：mail-邮寄，pickup-上门取件',
    express_company VARCHAR(50) COMMENT '快递公司',
    express_no VARCHAR(50) COMMENT '快递单号',

    -- 地址信息
    donor_address VARCHAR(500) COMMENT '捐赠者地址（用于上门取件）',
    receiver_address VARCHAR(500) COMMENT '接收地址',

    -- 捐赠统计
    total_items INT NOT NULL DEFAULT 0 COMMENT '物资总件数',
    estimated_value DECIMAL(10, 2) COMMENT '估算价值（元）',

    -- 状态
    status ENUM('created', 'paid', 'shipped', 'received', 'inspecting', 'stocked', 'allocated', 'delivered', 'signed', 'completed', 'abnormal')
        NOT NULL DEFAULT 'created' COMMENT '状态',

    -- 时间戳
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    -- 备注
    remark TEXT COMMENT '备注说明',

    INDEX idx_donor_id (donor_id),
    INDEX idx_status (status),
    INDEX idx_created_time (created_time),
    INDEX idx_donation_no (donation_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物资捐赠记录主表';


-- =====================================================
-- 2. 物资明细表
-- =====================================================
CREATE TABLE material_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '物资明细ID',
    donation_id BIGINT NOT NULL COMMENT '捐赠ID（关联material_donation表）',
    demand_id BIGINT COMMENT '需求ID（关联material_demand表，如匹配到需求）',

    -- 物资信息
    category VARCHAR(50) NOT NULL COMMENT '物资分类：clothing-衣物，book-书籍，stationery-学习用品，daily-生活用品，sports-体育用品，other-其他',
    category_detail VARCHAR(100) COMMENT '物资详细分类（如：冬装、故事书、书包）',
    name VARCHAR(200) NOT NULL COMMENT '物资名称',
    description VARCHAR(500) COMMENT '物资描述',
    quantity INT NOT NULL COMMENT '数量',
    unit VARCHAR(20) COMMENT '单位（件、本、套、个等）',

    -- 新旧程度
    condition_level ENUM('new', 'like_new', 'good', 'fair', 'poor')
        NOT NULL DEFAULT 'good' COMMENT '新旧程度：new-全新，like_new-九成新，good-良好，fair-一般，poor-较差',

    -- 价值估算
    estimated_value DECIMAL(10, 2) COMMENT '单件估算价值（元）',

    -- 照片
    photo_urls TEXT COMMENT '物资照片URL（多个用逗号分隔）',

    -- 检验结果
    inspection_result ENUM('pending', 'qualified', 'unqualified', 'need_process')
        DEFAULT 'pending' COMMENT '检验结果：pending-待检验，qualified-合格，unqualified-不合格，need_process-需处理',
    inspection_note VARCHAR(500) COMMENT '检验备注',

    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    INDEX idx_donation_id (donation_id),
    INDEX idx_demand_id (demand_id),
    INDEX idx_category (category),
    FOREIGN KEY (donation_id) REFERENCES material_donation(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物资明细表';


-- =====================================================
-- 3. 物资需求表
-- =====================================================
CREATE TABLE material_demand (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '需求ID',
    child_id BIGINT NOT NULL COMMENT '儿童ID（关联child表）',

    -- 需求信息
    category VARCHAR(50) NOT NULL COMMENT '物资分类',
    category_detail VARCHAR(100) COMMENT '详细分类',
    name VARCHAR(200) NOT NULL COMMENT '物资名称',
    description TEXT COMMENT '需求描述',

    -- 需求数量
    required_quantity INT NOT NULL COMMENT '需求数量',
    received_quantity INT NOT NULL DEFAULT 0 COMMENT '已接收数量',

    -- 紧急程度
    urgency_level ENUM('urgent', 'normal', 'stock') NOT NULL DEFAULT 'normal'
        COMMENT '紧急程度：urgent-紧急，normal-一般，stock-储备',

    -- 状态
    status ENUM('active', 'paused', 'completed') NOT NULL DEFAULT 'active'
        COMMENT '状态：active-活跃，paused-暂停，completed-已完成',

    -- 时间戳
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deadline DATE COMMENT '需求截止日期',

    INDEX idx_child_id (child_id),
    INDEX idx_category (category),
    INDEX idx_urgency (urgency_level),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物资需求表';


-- =====================================================
-- 4. 捐赠追踪记录表
-- =====================================================
CREATE TABLE donation_tracking (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '追踪ID',
    donation_id BIGINT NOT NULL COMMENT '捐赠ID（关联material_donation表）',

    -- 追踪节点信息
    node_type VARCHAR(50) NOT NULL COMMENT '节点类型：created, paid, shipped, received, inspecting, stocked, allocated, delivered, signed, completed, abnormal',
    node_name VARCHAR(100) NOT NULL COMMENT '节点名称',
    node_description VARCHAR(500) COMMENT '节点描述',

    -- 操作人员
    operator_id BIGINT COMMENT '操作员ID（关联user表）',
    operator_name VARCHAR(100) COMMENT '操作员姓名',

    -- 照片/视频
    photo_urls TEXT COMMENT '照片URL（多个用逗号分隔）',
    video_url VARCHAR(500) COMMENT '视频URL',

    -- 物流信息
    logistics_status VARCHAR(200) COMMENT '物流状态描述',
    logistics_location VARCHAR(200) COMMENT '物流位置',

    -- 备注
    note TEXT COMMENT '备注说明',

    -- 时间戳
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    INDEX idx_donation_id (donation_id),
    INDEX idx_created_time (created_time),
    INDEX idx_node_type (node_type),
    FOREIGN KEY (donation_id) REFERENCES material_donation(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='捐赠追踪记录表';


-- =====================================================
-- 5. 推荐日志表
-- =====================================================
CREATE TABLE recommendation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',

    -- 推荐对象
    child_id BIGINT NOT NULL COMMENT '儿童ID（关联child表）',

    -- 推荐目标
    donor_id BIGINT COMMENT '捐赠者ID（关联user表，NULL表示匿名）',

    -- 推荐来源
    source_type ENUM('homepage', 'search', 'category', 'region') NOT NULL COMMENT '推荐来源',
    source_params TEXT COMMENT '推荐参数（JSON格式）',

    -- 推荐结果
    score DECIMAL(5, 2) NOT NULL COMMENT '推荐得分',
    `rank` INT NOT NULL COMMENT '推荐排名',

    -- 推荐理由
    reason TEXT COMMENT '推荐理由',

    -- 转化结果
    action_taken ENUM('viewed', 'donated', 'ignored') DEFAULT 'viewed' COMMENT '用户操作',
    action_time DATETIME COMMENT '操作时间',

    -- 时间戳
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    INDEX idx_child_id (child_id),
    INDEX idx_donor_id (donor_id),
    INDEX idx_created_time (created_time),
    INDEX idx_score (score)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推荐日志表';


-- =====================================================
-- 6. 帮扶进度记录表
-- =====================================================
CREATE TABLE help_progress (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '进度ID',

    -- 关联信息
    child_id BIGINT NOT NULL COMMENT '儿童ID（关联child表）',
    project_id BIGINT COMMENT '项目ID（如属于某个项目）',
    donation_id BIGINT COMMENT '关联的捐赠ID（如涉及物资发放）',

    -- 活动信息
    activity_type VARCHAR(50) NOT NULL COMMENT '活动类型：home_visit-家访，tutoring-学业辅导，psychological-心理关怀，material_delivery-物资发放，activity-活动，other-其他',
    activity_title VARCHAR(200) NOT NULL COMMENT '活动标题',
    activity_description TEXT NOT NULL COMMENT '活动详细描述',

    -- 参与人员
    volunteer_ids TEXT COMMENT '参与志愿者ID列表（逗号分隔）',

    -- 状态
    status ENUM('draft', 'pending_review', 'published', 'rejected') NOT NULL DEFAULT 'pending_review'
        COMMENT '状态：draft-草稿，pending_review-待审核，published-已发布，rejected-已拒绝',

    -- 审核信息
    reviewer_id BIGINT COMMENT '审核人ID',
    review_time DATETIME COMMENT '审核时间',
    review_note VARCHAR(500) COMMENT '审核意见',

    -- 活动日期
    activity_date DATE NOT NULL COMMENT '活动日期',

    -- 效果评估
    outcome_summary TEXT COMMENT '效果总结',

    -- 时间戳
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    published_time DATETIME COMMENT '发布时间',

    INDEX idx_child_id (child_id),
    INDEX idx_project_id (project_id),
    INDEX idx_donation_id (donation_id),
    INDEX idx_activity_type (activity_type),
    INDEX idx_status (status),
    INDEX idx_activity_date (activity_date),
    INDEX idx_published_time (published_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帮扶进度记录表';


-- =====================================================
-- 7. 进度附件表（照片/视频）
-- =====================================================
CREATE TABLE progress_attachment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '附件ID',
    progress_id BIGINT NOT NULL COMMENT '进度ID（关联help_progress表）',

    -- 附件信息
    attachment_type ENUM('photo', 'video', 'document') NOT NULL COMMENT '附件类型',
    file_name VARCHAR(255) NOT NULL COMMENT '文件名',
    file_url VARCHAR(500) NOT NULL COMMENT '文件URL',
    file_size BIGINT COMMENT '文件大小（字节）',
    mime_type VARCHAR(100) COMMENT 'MIME类型',

    -- 照片信息（仅photo类型）
    description VARCHAR(500) COMMENT '照片描述',
    is_cover TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否为封面图（0-否，1-是）',
    display_order INT NOT NULL DEFAULT 0 COMMENT '显示顺序',

    -- 隐私保护
    privacy_processed TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已做隐私处理（0-否，1-是）',

    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    INDEX idx_progress_id (progress_id),
    INDEX idx_attachment_type (attachment_type),
    INDEX idx_is_cover (is_cover),
    FOREIGN KEY (progress_id) REFERENCES help_progress(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='进度附件表';


-- =====================================================
-- 8. 扩展 child 表（添加推荐权重字段）
-- =====================================================
-- 检查列是否存在，不存在则添加
SET @dbname = DATABASE();
SET @tablename = 'child';
SET @columnname = 'recommendation_weight';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      TABLE_SCHEMA = @dbname
      AND TABLE_NAME = @tablename
      AND COLUMN_NAME = @columnname
  ) > 0,
  'SELECT 1',
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' DECIMAL(5,2) NOT NULL DEFAULT 50.00 COMMENT ''推荐权重（0-100）''')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 添加其他推荐相关字段（如果不存在）
SET @columnname = 'family_income_level';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      TABLE_SCHEMA = @dbname
      AND TABLE_NAME = @tablename
      AND COLUMN_NAME = @columnname
  ) > 0,
  'SELECT 1',
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' ENUM(''low'', ''medium'', ''high'') COMMENT ''家庭收入水平：low-低，medium-中，high-高''')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

SET @columnname = 'family_condition';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      TABLE_SCHEMA = @dbname
      AND TABLE_NAME = @tablename
      AND COLUMN_NAME = @columnname
  ) > 0,
  'SELECT 1',
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' VARCHAR(500) COMMENT ''家庭情况描述''')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

SET @columnname = 'urgent_need';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      TABLE_SCHEMA = @dbname
      AND TABLE_NAME = @tablename
      AND COLUMN_NAME = @columnname
  ) > 0,
  'SELECT 1',
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' TEXT COMMENT ''紧急需求描述''')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- =====================================================
-- 9. 插入测试数据（可选）
-- =====================================================

-- 插入物资需求测试数据
INSERT INTO material_demand (child_id, category, category_detail, name, description, required_quantity, urgency_level) VALUES
(1, 'stationery', 'schoolbag', '书包', '需要一个新书包用于上学', 1, 'urgent'),
(1, 'book', 'story', '故事书', '需要适合小学三年级的故事书', 5, 'normal'),
(2, 'clothing', 'winter', '冬装', '需要保暖的冬装', 2, 'urgent'),
(3, 'stationery', 'basic', '学习文具', '需要基本的学习用品（笔、本子等）', 10, 'normal');

-- 插入物资捐赠测试数据
INSERT INTO material_donation (donation_no, donor_name, donor_phone, donation_method, total_items, estimated_value, status) VALUES
('MD202501250001', '张三', '13800138000', 'mail', 5, 200.00, 'created'),
('MD202501250002', '李四', '13800138001', 'pickup', 10, 500.00, 'received');

-- 插入物资明细测试数据
INSERT INTO material_item (donation_id, category, name, quantity, unit, condition_level, estimated_value) VALUES
(1, 'book', '故事书', 5, '本', 'like_new', 30.00),
(1, 'stationery', '铅笔', 20, '支', 'new', 20.00),
(2, 'clothing', '冬装', 3, '件', 'good', 300.00),
(2, 'stationery', '书包', 2, '个', 'new', 200.00);

-- 插入帮扶进度测试数据
INSERT INTO help_progress (child_id, activity_type, activity_title, activity_description, volunteer_ids, activity_date, status) VALUES
(1, 'home_visit', '期初家访', '了解学生新学期学习情况和生活状况，提供必要的帮助和指导。', '1,2', '2025-01-20', 'published'),
(2, 'material_delivery', '物资发放', '发放捐赠的衣物和学习用品', '1', '2025-01-18', 'published');

-- 插入进度附件测试数据
INSERT INTO progress_attachment (progress_id, attachment_type, file_name, file_url, description, is_cover, display_order) VALUES
(1, 'photo', 'visit_photo_1.jpg', '/uploads/progress/visit_1.jpg', '家访照片1', 1, 1),
(1, 'photo', 'visit_photo_2.jpg', '/uploads/progress/visit_2.jpg', '家访照片2', 0, 2),
(2, 'photo', 'delivery_photo.jpg', '/uploads/progress/delivery_1.jpg', '物资发放照片', 1, 1);


-- =====================================================
-- 10. 创建视图（方便查询）
-- =====================================================

-- 捐赠统计视图
CREATE OR REPLACE VIEW v_donation_stats AS
SELECT
    md.donor_id,
    md.donor_name,
    COUNT(md.id) AS donation_count,
    SUM(md.total_items) AS total_items,
    SUM(md.estimated_value) AS total_value,
    COUNT(CASE WHEN md.status = 'completed' THEN 1 END) AS completed_count
FROM material_donation md
GROUP BY md.donor_id, md.donor_name;

-- 帮扶进度统计视图
CREATE OR REPLACE VIEW v_progress_stats AS
SELECT
    hp.child_id,
    COUNT(hp.id) AS progress_count,
    COUNT(CASE WHEN hp.activity_type = 'home_visit' THEN 1 END) AS home_visit_count,
    COUNT(CASE WHEN hp.activity_type = 'material_delivery' THEN 1 END) AS material_delivery_count,
    COUNT(CASE WHEN hp.activity_type = 'tutoring' THEN 1 END) AS tutoring_count,
    MIN(hp.activity_date) AS first_activity_date,
    MAX(hp.activity_date) AS last_activity_date
FROM help_progress hp
WHERE hp.status = 'published'
GROUP BY hp.child_id;

-- 需求满足情况视图
CREATE OR REPLACE VIEW v_demand_fulfillment AS
SELECT
    mdemand.child_id,
    mdemand.category,
    COUNT(mdemand.id) AS demand_count,
    SUM(mdemand.required_quantity) AS total_required,
    SUM(mdemand.received_quantity) AS total_received,
    ROUND(SUM(mdemand.received_quantity) / SUM(mdemand.required_quantity) * 100, 2) AS fulfillment_rate
FROM material_demand mdemand
WHERE mdemand.status = 'active'
GROUP BY mdemand.child_id, mdemand.category;


-- =====================================================
-- 完成！
-- =====================================================
SELECT '物资捐赠与追踪系统数据库表创建成功！' AS message;
SELECT COUNT(*) AS created_tables FROM information_schema.tables WHERE table_schema = 'school' AND table_name IN ('material_donation', 'material_item', 'material_demand', 'donation_tracking', 'recommendation_log', 'help_progress', 'progress_attachment');
