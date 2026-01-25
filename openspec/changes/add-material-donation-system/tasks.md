# 实施任务清单

## 1. 数据库设计与开发
- [ ] 1.1 设计 `material_donation` 表（物资捐赠记录主表）
- [ ] 1.2 设计 `material_item` 表（物资明细）
- [ ] 1.3 设计 `material_demand` 表（物资需求）
- [ ] 1.4 设计 `donation_tracking` 表（捐赠追踪记录）
- [ ] 1.5 设计 `recommendation_log` 表（推荐日志）
- [ ] 1.6 设计 `help_progress` 表（帮扶进度记录）
- [ ] 1.7 设计 `progress_attachment` 表（进度附件）
- [ ] 1.8 扩展 `child` 表（添加推荐权重字段）
- [ ] 1.9 创建数据库迁移脚本
- [ ] 1.10 准备测试数据

## 2. 后端开发 - 实体层
- [ ] 2.1 创建 MaterialDonation 实体类
- [ ] 2.2 创建 MaterialItem 实体类
- [ ] 2.3 创建 MaterialDemand 实体类
- [ ] 2.4 创建 DonationTracking 实体类
- [ ] 2.5 创建 RecommendationLog 实体类
- [ ] 2.6 创建 HelpProgress 实体类
- [ ] 2.7 创建 ProgressAttachment 实体类
- [ ] 2.8 扩展 Child 实体类
- [ ] 2.9 配置 JPA 关系映射

## 3. 后端开发 - Repository 层
- [ ] 3.1 创建 MaterialDonationRepository
- [ ] 3.2 创建 MaterialDemandRepository
- [ ] 3.3 创建 DonationTrackingRepository
- [ ] 3.4 创建 RecommendationLogRepository
- [ ] 3.5 创建 HelpProgressRepository
- [ ] 3.6 创建 ProgressAttachmentRepository
- [ ] 3.7 扩展 ChildRepository（添加推荐查询方法）

## 4. 后端开发 - Service 层
- [ ] 4.1 创建 MaterialDonationService
  - [ ] 4.1.1 实现物资捐赠创建
  - [ ] 4.1.2 实现物资捐赠查询
  - [ ] 4.1.3 实现物资状态更新
- [ ] 4.2 创建 MaterialDemandService
  - [ ] 4.2.1 实现需求发布
  - [ ] 4.2.2 实现需求匹配
- [ ] 4.3 创建 RecommendationService
  - [ ] 4.3.1 实现推荐算法（基于权重排序）
  - [ ] 4.3.2 实现多维度筛选
  - [ ] 4.3.3 实现推荐理由生成
- [ ] 4.4 create TrackingService
  - [ ] 4.4.1 实现追踪节点创建
  - [ ] 4.4.2 实现追踪状态更新
  - [ ] 4.4.3 实现时间线查询
- [ ] 4.5 创建 ProgressService
  - [ ] 4.5.1 实现进度记录创建
  - [ ] 4.5.2 实现进度发布
  - [ ] 4.5.3 实现进度查询
- [ ] 4.6 创建 FileUploadService
  - [ ] 4.6.1 实现照片上传
  - [ ] 4.6.2 实现图片压缩
  - [ ] 4.6.3 实现文件存储

## 5. 后端开发 - Controller 层
- [ ] 5.1 创建 MaterialDonationController
  - [ ] 5.1.1 POST /api/material-donations - 创建捐赠
  - [ ] 5.1.2 GET /api/material-donations - 查询捐赠列表
  - [ ] 5.1.3 GET /api/material-donations/{id} - 查询捐赠详情
  - [ ] 5.1.4 PUT /api/material-donations/{id}/status - 更新状态
- [ ] 5.2 创建 MaterialDemandController
  - [ ] 5.2.1 GET /api/material-demands - 获取物资需求清单
  - [ ] 5.2.2 GET /api/material-demands/{id} - 获取需求详情
- [ ] 5.3 创建 RecommendationController
  - [ ] 5.3.1 GET /api/recommendations - 获取推荐对象列表
  - [ ] 5.3.2 GET /api/recommendations/filter - 按条件筛选
- [ ] 5.4 创建 TrackingController
  - [ ] 5.4.1 GET /api/tracking/{donationId} - 查询物资追踪时间线
  - [ ] 5.4.2 POST /api/tracking/{donationId}/nodes - 添加追踪节点
- [ ] 5.5 创建 ProgressController
  - [ ] 5.5.1 GET /api/progress/child/{childId} - 查询儿童帮扶进度
  - [ ] 5.5.2 GET /api/progress/project/{projectId} - 查询项目进度
  - [ ] 5.5.3 POST /api/progress - 创建进度记录（管理员）
- [ ] 5.6 创建 FileUploadController
  - [ ] 5.6.1 POST /api/upload - 上传文件

## 6. 前端网站开发
- [ ] 6.1 物资捐赠页面
  - [ ] 6.1.1 设计物资分类选择 UI
  - [ ] 6.1.2 实现物资信息表单
  - [ ] 6.1.3 实现图片上传预览
  - [ ] 6.1.4 实现捐赠方式选择
  - [ ] 6.1.5 实现提交确认
- [ ] 6.2 帮扶对象推荐页面
  - [ ] 6.2.1 设计推荐卡片 UI
  - [ ] 6.2.2 实现筛选器组件
  - [ ] 6.2.3 实现推荐理由展示
  - [ ] 6.2.4 实现快速捐赠入口
- [ ] 6.3 物资追踪页面
  - [ ] 6.3.1 设计时间轴组件
  - [ ] 6.3.2 实现追踪状态展示
  - [ ] 6.3.3 实现物流信息集成
  - [ ] 6.3.4 实现照片预览
- [ ] 6.4 帮扶进度公开页面
  - [ ] 6.4.1 设计进度可视化组件
  - [ ] 6.4.2 实现进度时间线
  - [ ] 6.4.3 实现照片墙展示
  - [ ] 6.4.4 实现数据统计卡片
- [ ] 6.5 API 集成
  - [ ] 6.5.1 创建 material-donation API 模块
  - [ ] 6.5.2 创建 recommendation API 模块
  - [ ] 6.5.3 创建 tracking API 模块
  - [ ] 6.5.4 创建 progress API 模块

## 7. 管理后台开发
- [ ] 7.1 物资管理模块
  - [ ] 7.1.1 物资捐赠列表页
  - [ ] 7.1.2 物资详情页
  - [ ] 7.1.3 物资审核页
  - [ ] 7.1.4 物资入库登记
- [ ] 7.2 推荐配置模块
  - [ ] 7.2.1 推荐权重设置
  - [ ] 7.2.2 推荐规则配置
  - [ ] 7.2.3 推荐日志查看
- [ ] 7.3 追踪管理模块
  - [ ] 7.3.1 追踪记录列表
  - [ ] 7.3.2 追踪节点添加
  - [ ] 7.3.3 追踪状态更新
- [ ] 7.4 进度管理模块
  - [ ] 7.4.1 进度记录列表
  - [ ] 7.4.2 进度发布表单
  - [ ] 7.4.3 附件管理
  - [ ] 7.4.4 进度统计分析

## 8. 推荐算法实现
- [ ] 8.1 设计推荐权重计算公式
  - [ ] 8.1.1 确定紧急程度权重
  - [ ] 8.1.2 确定需求匹配度权重
  - [ ] 8.1.3 确定地区优先级权重
  - [ ] 8.1.4 确定时间紧迫性权重
- [ ] 8.2 实现推荐算法
  - [ ] 8.2.1 计算综合得分
  - [ ] 8.2.2 排序和分页
  - [ ] 8.2.3 生成推荐理由
- [ ] 8.3 算法优化
  - [ ] 8.3.1 性能优化（缓存、索引）
  - [ ] 8.3.2 算法调参

## 9. 测试
- [ ] 9.1 单元测试
  - [ ] 9.1.1 Service 层测试
  - [ ] 9.1.2 Repository 层测试
- [ ] 9.2 集成测试
  - [ ] 9.2.1 API 测试
  - [ ] 9.2.2 推荐算法测试
- [ ] 9.3 前端测试
  - [ ] 9.3.1 组件测试
  - [ ] 9.3.2 E2E 测试
- [ ] 9.4 手动测试
  - [ ] 9.4.1 完整捐赠流程测试
  - [ ] 9.4.2 推荐功能测试
  - [ ] 9.4.3 追踪功能测试

## 10. 部署与文档
- [ ] 10.1 数据库迁移脚本执行
- [ ] 10.2 后端服务部署
- [ ] 10.3 前端构建和部署
- [ ] 10.4 API 文档更新（Swagger）
- [ ] 10.5 用户使用手册编写
