-- =====================================================
-- 物资捐赠与追踪系统 - 表结构更新脚本
-- 创建时间: 2025-01-25
-- 说明: 仅创建缺失的表，不删除现有数据
-- =====================================================

USE school;

-- =====================================================
-- 1. 创建缺失的表（检查后创建）
-- =====================================================

-- 捐赠追踪记录表
CREATE TABLE IF NOT EXISTS donation_tracking (
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


-- 推荐日志表
CREATE TABLE IF NOT EXISTS recommendation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',

    -- 推荐对象
    child_id BIGINT NOT NULL COMMENT '儿童ID（关联student表）',

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


-- 帮扶进度记录表
CREATE TABLE IF NOT EXISTS help_progress (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '进度ID',

    -- 关联信息
    child_id BIGINT NOT NULL COMMENT '儿童ID（关联student表）',
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


-- 进度附件表（照片/视频）
CREATE TABLE IF NOT EXISTS progress_attachment (
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
-- 2. 扩展 student 表（添加推荐权重字段）
-- =====================================================

-- 检查并添加字段
SET @dbname = DATABASE();
SET @tablename = 'student';

-- 推荐权重
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

-- 家庭收入水平
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

-- 家庭情况描述
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

-- 紧急需求描述
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
-- 3. 插入测试数据
-- =====================================================

-- 检查是否已有测试数据
SET @test_data_exists = (SELECT COUNT(*) FROM material_demand LIMIT 1);

-- 如果没有测试数据，则插入
SET @insert_test_data = IF(@test_data_exists = 0,
  'INSERT INTO material_demand (child_id, category, category_detail, name, description, required_quantity, urgency_level) VALUES
  (1, ''stationery'', ''schoolbag'', ''书包'', ''需要一个新书包用于上学'', 1, ''urgent''),
  (1, ''book'', ''story'', ''故事书'', ''需要适合小学三年级的故事书'', 5, ''normal''),
  (2, ''clothing'', ''winter'', ''冬装'', ''需要保暖的冬装'', 2, ''urgent''),
  (3, ''stationery'', ''basic'', ''学习文具'', ''需要基本的学习用品（笔、本子等）'', 10, ''normal'')',
  'SELECT ''测试数据已存在，跳过插入'''
);

PREPARE stmt FROM @insert_test_data;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 捐赠测试数据
SET @test_data_exists = (SELECT COUNT(*) FROM material_donation WHERE donation_no LIKE 'MD2025%' LIMIT 1);

SET @insert_test_data = IF(@test_data_exists = 0,
  'INSERT INTO material_donation (donation_no, donor_name, donor_phone, donation_method, total_items, estimated_value, status) VALUES
  (''MD202501250001'', ''张三'', ''13800138000'', ''mail'', 5, 200.00, ''created''),
  (''MD202501250002'', ''李四'', ''13800138001'', ''pickup'', 10, 500.00, ''received'')',
  'SELECT ''捐赠测试数据已存在，跳过插入'''
);

PREPARE stmt FROM @insert_test_data;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


-- =====================================================
-- 4. 创建视图
-- =====================================================

-- 捐赠统计视图
DROP VIEW IF EXISTS v_donation_stats;
CREATE VIEW v_donation_stats AS
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
DROP VIEW IF EXISTS v_progress_stats;
CREATE VIEW v_progress_stats AS
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
DROP VIEW IF EXISTS v_demand_fulfillment;
CREATE VIEW v_demand_fulfillment AS
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
-- 完成！显示结果
-- =====================================================
SELECT '物资捐赠与追踪系统数据库表更新成功！' AS message;

SELECT
    'material_donation' AS table_name,
    TABLE_ROWS AS row_count
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'school' AND TABLE_NAME = 'material_donation'

UNION ALL

SELECT
    'material_item',
    TABLE_ROWS
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'school' AND TABLE_NAME = 'material_item'

UNION ALL

SELECT
    'material_demand',
    TABLE_ROWS
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'school' AND TABLE_NAME = 'material_demand'

UNION ALL

SELECT
    'donation_tracking',
    TABLE_ROWS
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'school' AND TABLE_NAME = 'donation_tracking'

UNION ALL

SELECT
    'recommendation_log',
    TABLE_ROWS
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'school' AND TABLE_NAME = 'recommendation_log'

UNION ALL

SELECT
    'help_progress',
    TABLE_ROWS
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'school' AND TABLE_NAME = 'help_progress'

UNION ALL

SELECT
    'progress_attachment',
    TABLE_ROWS
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'school' AND TABLE_NAME = 'progress_attachment';
