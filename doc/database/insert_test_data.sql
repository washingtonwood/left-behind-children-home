-- =====================================================
-- 物资捐赠与追踪系统 - 测试数据插入脚本
-- 创建时间: 2025-01-25
-- 说明: 为空表插入完整的测试数据
-- =====================================================

USE school;

-- =====================================================
-- 1. 物资明细表（material_item）
-- =====================================================

-- 为捐赠 ID=1（张三的捐赠）添加物资明细
INSERT INTO material_item (donation_id, demand_id, category, category_detail, name, description, quantity, unit, condition_level, estimated_value, inspection_result) VALUES
(1, 2, 'book', 'story', '安徒生童话', '适合小学三年级的故事书，全新', 3, '本', 'new', 45.00, 'qualified'),
(1, 1, 'stationery', 'schoolbag', '双肩书包', '蓝色儿童书包，九成新', 1, '个', 'like_new', 80.00, 'qualified'),
(1, NULL, 'stationery', 'basic', '铅笔套装', '12支装铅笔，全新', 1, '套', 'new', 15.00, 'qualified');

-- 为捐赠 ID=2（李四的捐赠）添加物资明细
INSERT INTO material_item (donation_id, demand_id, category, category_detail, name, description, quantity, unit, condition_level, estimated_value, inspection_result) VALUES
(2, 3, 'clothing', 'winter', '儿童羽绒服', '适合120-130cm，粉色，全新', 2, '件', 'new', 300.00, 'qualified'),
(2, 4, 'stationery', 'basic', '文具大礼包', '包含笔、本子、橡皮等', 10, '套', 'new', 200.00, 'qualified');


-- =====================================================
-- 2. 捐赠追踪记录表（donation_tracking）
-- =====================================================

-- 为捐赠 ID=1 添加追踪记录
INSERT INTO donation_tracking (donation_id, node_type, node_name, node_description, operator_name, photo_urls, created_time) VALUES
(1, 'created', '已创建', '捐赠登记成功', '系统', NULL, '2025-01-20 10:00:00'),
(1, 'shipped', '已发货', '捐赠者已通过顺丰快递发货', '张三', '/uploads/tracking/md1_package.jpg', '2025-01-21 14:30:00'),
(1, 'received', '已接收', '志愿者已收到物资包裹', '王志愿者', '/uploads/tracking/md1_received.jpg', '2025-01-23 10:15:00'),
(1, 'inspecting', '检验中', '正在检验物资质量', '李检验员', NULL, '2025-01-23 10:20:00'),
(1, 'stocked', '已入库', '物资检验合格，已入库', '王志愿者', '/uploads/tracking/md1_stocked.jpg', '2025-01-23 11:00:00'),
(1, 'allocated', '分配中', '已匹配到受助儿童，准备发放', '系统', NULL, '2025-01-24 09:00:00');

-- 为捐赠 ID=2 添加追踪记录
INSERT INTO donation_tracking (donation_id, node_type, node_name, node_description, operator_name, photo_urls, logistics_status, created_time) VALUES
(2, 'created', '已创建', '捐赠登记成功', '系统', NULL, NULL, '2025-01-22 09:00:00'),
(2, 'received', '已接收', '志愿者上门取件成功', '赵志愿者', '/uploads/tracking/md2_pickup.jpg', '已到站点', '2025-01-23 15:00:00'),
(2, 'stocked', '已入库', '物资已入库', '赵志愿者', '/uploads/tracking/md2_stocked.jpg', NULL, '2025-01-23 16:00:00');


-- =====================================================
-- 3. 推荐日志表（recommendation_log）
-- =====================================================

-- 模拟推荐日志
INSERT INTO recommendation_log (child_id, donor_id, source_type, source_params, score, `rank`, reason, action_taken, action_time, created_time) VALUES
-- 儿童ID=1 的推荐
(1, NULL, 'homepage', '{"urgency":"urgent","category":"stationery"}', 92.5, 1, '该儿童家庭困难，急需学习用品。您的书包捐赠正是孩子最需要的帮助。', 'viewed', '2025-01-25 10:30:00', '2025-01-25 10:00:00'),
(1, NULL, 'search', '{"region":"贵州","age":"8-10"}', 88.3, 2, '该儿童成绩优秀但家庭困难，希望得到更多帮助。', 'viewed', NULL, '2025-01-25 11:00:00'),

-- 儿童ID=2 的推荐
(2, NULL, 'homepage', '{"urgency":"urgent","category":"clothing"}', 90.8, 1, '天气转冷，该儿童急需冬装保暖。您的衣物捐赠将温暖孩子整个冬天。', 'donated', '2025-01-25 12:00:00', '2025-01-25 09:30:00'),
(2, NULL, 'region', '{"province":"四川"}', 85.2, 3, '该地区偏远，儿童物资匮乏，您的帮助意义重大。', 'viewed', NULL, '2025-01-25 13:00:00'),

-- 儿童ID=3 的推荐
(3, NULL, 'homepage', '{"category":"stationery"}', 82.5, 3, '该儿童学习努力，需要基本学习用品支持。', 'viewed', NULL, '2025-01-25 14:00:00'),
(3, NULL, 'search', '{"urgency":"normal"}', 78.9, 5, '该儿童条件一般，但在等待帮扶中。', 'ignored', NULL, '2025-01-25 15:00:00'),

-- 儿童ID=4 的推荐（假设有第4个学生）
(4, NULL, 'homepage', '{}', 75.0, 4, '该儿童需要长期帮扶支持。', 'viewed', NULL, '2025-01-25 16:00:00');


-- =====================================================
-- 4. 帮扶进度记录表（help_progress）
-- =====================================================

INSERT INTO help_progress (child_id, project_id, donation_id, activity_type, activity_title, activity_description, volunteer_ids, status, activity_date, outcome_summary, created_time, published_time) VALUES
-- 儿童ID=1 的帮扶进度
(1, NULL, 1, 'home_visit', '期初家访', '2025年1月20日，志愿者王明、李华到学生家中进行家访，了解学生的学习情况和生活状况。学生家庭条件困难，但学习态度端正，成绩优秀。', '1,2', 'published', '2025-01-20', '学生精神状态良好，学习积极性高。家庭需要持续关注和支持。', '2025-01-20 18:00:00', '2025-01-21 09:00:00'),

(1, NULL, NULL, 'tutoring', '学业辅导', '志愿者每周六下午为学生提供2小时学业辅导，主要辅导数学和语文。本月已完成4次辅导，学生成绩有明显提升。', '1', 'published', '2025-01-25', '数学成绩从85分提升到92分，学生对学习更有信心了。', '2025-01-25 20:00:00', '2025-01-25 21:00:00'),

(1, NULL, 1, 'material_delivery', '物资发放', '志愿者到学校为学生发放捐赠的书包和学习用品，学生非常开心，表示会好好爱惜和使用这些物品。', '2', 'published', '2025-01-24', '物资已发放到位，学生表示感谢。', '2025-01-24 17:00:00', '2025-01-24 18:00:00'),

-- 儿童ID=2 的帮扶进度
(2, NULL, 2, 'material_delivery', '冬装发放', '志愿者将捐赠的羽绒服送到学生家中，现场试穿尺寸合适，学生和家长都非常满意。', '3', 'published', '2025-01-23', '冬装合身，学生很高兴，表示冬天不会再冷了。', '2025-01-23 16:00:00', '2025-01-23 17:00:00'),

(2, NULL, NULL, 'psychological', '心理关怀', '心理咨询师张老师与学生进行了深入交流，了解学生的心理状态，给予情感支持和鼓励。', '4', 'published', '2025-01-22', '学生情绪稳定，对未来充满希望。', '2025-01-22 15:00:00', '2025-01-22 16:00:00'),

-- 儿童ID=3 的帮扶进度
(3, NULL, 2, 'material_delivery', '文具发放', '在学校为学生发放文具大礼包，包含笔、本子、橡皮等日常学习用品。', '1', 'published', '2025-01-24', '学生收到文具后非常开心，表示会努力学习。', '2025-01-24 14:00:00', '2025-01-24 15:00:00'),

(3, NULL, NULL, 'home_visit', '家庭走访', '志愿者到学生家中了解家庭情况，与学生监护人交流，了解学生在家的学习生活状况。', '2', 'published', '2025-01-19', '家庭需要关注，学生学习环境有待改善。', '2025-01-19 17:00:00', '2025-01-19 18:00:00');


-- =====================================================
-- 5. 进度附件表（progress_attachment）
-- =====================================================

-- 为进度ID=1（家访）添加照片
INSERT INTO progress_attachment (progress_id, attachment_type, file_name, file_url, description, is_cover, display_order, privacy_processed) VALUES
(1, 'photo', 'home_visit_1.jpg', '/uploads/progress/20250120_home_1.jpg', '志愿者与学生交流', 1, 1, 1),
(1, 'photo', 'home_visit_2.jpg', '/uploads/progress/20250120_home_2.jpg', '学生学习环境', 0, 2, 1),
(1, 'photo', 'home_visit_3.jpg', '/uploads/progress/20250120_home_3.jpg', '与家长交流', 0, 3, 1);

-- 为进度ID=2（学业辅导）添加照片
INSERT INTO progress_attachment (progress_id, attachment_type, file_name, file_url, description, is_cover, display_order, privacy_processed) VALUES
(2, 'photo', 'tutoring_1.jpg', '/uploads/progress/20250125_tutor_1.jpg', '志愿者辅导学生作业', 1, 1, 1),
(2, 'photo', 'tutoring_2.jpg', '/uploads/progress/20250125_tutor_2.jpg', '学生认真学习', 0, 2, 1);

-- 为进度ID=3（物资发放）添加照片
INSERT INTO progress_attachment (progress_id, attachment_type, file_name, file_url, description, is_cover, display_order, privacy_processed) VALUES
(3, 'photo', 'delivery_1.jpg', '/uploads/progress/20250124_delivery_1.jpg', '发放书包和学习用品', 1, 1, 1),
(3, 'photo', 'delivery_2.jpg', '/uploads/progress/20250124_delivery_2.jpg', '学生收到礼物很开心', 0, 2, 1);

-- 为进度ID=4（冬装发放）添加照片
INSERT INTO progress_attachment (progress_id, attachment_type, file_name, file_url, description, is_cover, display_order, privacy_processed) VALUES
(4, 'photo', 'winter_coat_1.jpg', '/uploads/progress/20250123_coat_1.jpg', '志愿者帮助学生试穿羽绒服', 1, 1, 1),
(4, 'photo', 'winter_coat_2.jpg', '/uploads/progress/20250123_coat_2.jpg', '穿上羽绒服很暖和', 0, 2, 1);

-- 为进度ID=5（心理关怀）添加照片
INSERT INTO progress_attachment (progress_id, attachment_type, file_name, file_url, description, is_cover, display_order, privacy_processed) VALUES
(5, 'photo', 'psychological_1.jpg', '/uploads/progress/20250122_psych_1.jpg', '心理咨询师与学生交流', 1, 1, 1);

-- 为进度ID=6（文具发放）添加照片
INSERT INTO progress_attachment (progress_id, attachment_type, file_name, file_url, description, is_cover, display_order, privacy_processed) VALUES
(6, 'photo', 'stationery_1.jpg', '/uploads/progress/20250124_stationery_1.jpg', '发放文具礼包', 1, 1, 1);

-- 为进度ID=7（家庭走访）添加照片
INSERT INTO progress_attachment (progress_id, attachment_type, file_name, file_url, description, is_cover, display_order, privacy_processed) VALUES
(7, 'photo', 'family_visit_1.jpg', '/uploads/progress/20250119_visit_1.jpg', '志愿者家访', 1, 1, 1),
(7, 'photo', 'family_visit_2.jpg', '/uploads/progress/20250119_visit_2.jpg', '了解家庭情况', 0, 2, 1);


-- =====================================================
-- 6. 更新 material_demand 表的 received_quantity
-- =====================================================

-- 更新需求ID=1（书包）已接收1件
UPDATE material_demand SET received_quantity = 1 WHERE id = 1;

-- 更新需求ID=2（故事书）已接收3本
UPDATE material_demand SET received_quantity = 3 WHERE id = 2;

-- 更新需求ID=3（冬装）已接收2件
UPDATE material_demand SET received_quantity = 2 WHERE id = 3;

-- 更新需求ID=4（学习文具）已接收10套
UPDATE material_demand SET received_quantity = 10 WHERE id = 4;


-- =====================================================
-- 7. 更新 student 表的推荐权重（模拟数据）
-- =====================================================

-- 为前3个学生设置不同的推荐权重
UPDATE student SET recommendation_weight = 92.5, family_income_level = 'low', family_condition = '父母在外务工，与祖母生活，家庭经济困难', urgent_need = '急需学习用品和冬装' WHERE id = 1;

UPDATE student SET recommendation_weight = 90.8, family_income_level = 'low', family_condition = '单亲家庭，母亲独自抚养，收入微薄', urgent_need = '急需保暖衣物' WHERE id = 2;

UPDATE student SET recommendation_weight = 82.5, family_income_level = 'medium', family_condition = '父母在外打工，与祖父生活，条件一般', urgent_need = '需要基本学习用品' WHERE id = 3;


-- =====================================================
-- 完成！显示统计信息
-- =====================================================

SELECT '测试数据插入成功！' AS message;

SELECT '各表数据统计：' AS info;

SELECT
    'material_donation' AS table_name,
    COUNT(*) AS row_count
FROM material_donation
UNION ALL
SELECT
    'material_item',
    COUNT(*)
FROM material_item
UNION ALL
SELECT
    'material_demand',
    COUNT(*)
FROM material_demand
UNION ALL
SELECT
    'donation_tracking',
    COUNT(*)
FROM donation_tracking
UNION ALL
SELECT
    'recommendation_log',
    COUNT(*)
FROM recommendation_log
UNION ALL
SELECT
    'help_progress',
    COUNT(*)
FROM help_progress
UNION ALL
SELECT
    'progress_attachment',
    COUNT(*)
FROM progress_attachment;
