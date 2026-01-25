# 项目上下文

## 目的
守护童行 - 留守儿童救助平台是一个面向公益组织的综合管理系统，采用 **前后端分离架构**，由三个核心部分组成：

### 1. 前端展示网站（Vue.js）
面向公众的官方网站，提供：
- 公益宣传和留守儿童故事展示
- 在线捐赠和善款追踪
- 志愿者招募和活动报名
- 项目进展实时展示
- 透明公示和财务报告

### 2. 管理后台（Vue.js + Element UI/Ant Design Vue）
内部管理系统，功能包括：
- 受助儿童信息管理（增删改查、档案管理）
- 志愿者管理和活动安排
- 捐赠记录和财务管理
- 救助项目管理和执行跟踪
- 新闻公告和通知发布
- 数据统计和可视化分析
- 系统用户权限管理

### 3. 后端服务（Spring Boot）
提供 RESTful API，处理：
- 用户认证和授权（JWT）
- 业务逻辑处理
- 数据持久化（MySQL）
- 支付接口集成
- 文件上传和管理
- 数据导出和报表生成

## 技术栈

### 前端技术栈

#### 前端展示网站
- **Vue 3** - 渐进式 JavaScript 框架
- **Vue Router** - 路由管理
- **Pinia** - 状态管理
- **Vite** - 构建工具
- **Axios** - HTTP 客户端
- **Tailwind CSS** - 实用优先 CSS 框架
- **Element Plus** 或 **Ant Design Vue** - UI 组件库

#### 管理后台
- **Vue 3** - 核心框架
- **Vue Router** - 路由管理
- **Pinia** - 状态管理
- **Element Plus** 或 **Ant Design Vue** - 企业级 UI 组件库
- **Vite** - 构建工具
- **Axios** - HTTP 客户端
- **ECharts** 或 **Chart.js** - 数据可视化
- **VueUse** - Vue 组合式工具库

### 后端技术栈
- **Java 17** 或 **Java 21** - 编程语言
- **Spring Boot 3.x** - 应用框架
  - **Spring Web** - RESTful API
  - **Spring Security** - 安全认证（JWT）
  - **Spring Data JPA** - 数据持久化
  - **Spring Validation** - 数据验证
  - **Spring Cache** - 缓存管理
- **MyBatis-Plus** - ORM 框架（可选，或使用 JPA）
- **MySQL 8.0** - 关系型数据库
- **Redis** - 缓存和会话存储
- **Druid** 或 **HikariCP** - 数据库连接池

### 开发工具
- **Maven** 或 **Gradle** - 项目构建
- **Git** - 版本控制
- **IntelliJ IDEA** - Java IDE
- **VS Code** - 前端开发
- **Postman** 或 **Apifox** - API 测试
- **Navicat** 或 **DBeaver** - 数据库管理

### 部署架构
```
┌─────────────────┐     ┌─────────────────┐
│   前端网站      │     │   管理后台      │
│   (Vue 3)       │     │   (Vue 3)       │
│   Nginx/CDN     │     │   Nginx         │
└────────┬────────┘     └────────┬────────┘
         │                       │
         └───────────┬───────────┘
                     │
         ┌───────────▼───────────┐
         │   Spring Boot API    │
         │   (端口: 8080)        │
         └───────────┬───────────┘
                     │
         ┌───────────▼───────────┐
         │   MySQL 8.0          │
         │   Redis              │
         └──────────────────────┘
```

## 项目约定

### 代码风格

#### Java 后端规范
- **命名规范**：
  - 类名：帕斯卡命名法（PascalCase）- `UserService`
  - 方法名：驼峰命名法（camelCase）- `getUserById`
  - 常量：全大写下划线 - `MAX_PAGE_SIZE`
  - 包名：全小写 - `com.shourutongxing.controller`

- **分层架构**：
  ```
  com.shourutongxing/
  ├── controller/      # 控制器层（处理 HTTP 请求）
  ├── service/         # 服务层（业务逻辑）
  │   └── impl/       # 服务实现
  ├── mapper/          # 数据访问层（MyBatis）
  ├── entity/          # 实体类（数据库表映射）
  ├── dto/             # 数据传输对象
  ├── vo/              # 视图对象（返回前端）
  ├── config/          # 配置类
  ├── exception/       # 异常处理
  ├── util/            # 工具类
  └── ShourutongxingApplication.java
  ```

- **注解使用**：
  - `@RestController` - REST API 控制器
  - `@RequestMapping` - 类级别路由
  - `@GetMapping/@PostMapping` - 方法级别路由
  - `@Service` - 服务层
  - `@Mapper` - MyBatis 映射器
  - `@Entity` - JPA 实体
  - `@Transactional` - 事务管理

#### Vue 前端规范
- **组件命名**：帕斯卡命名法 - `UserProfile.vue`
- **文件组织**：
  ```
  src/
  ├── views/           # 页面组件
  ├── components/      # 可复用组件
  ├── router/          # 路由配置
  ├── store/           # Pinia 状态管理
  ├── api/             # API 请求封装
  ├── utils/           # 工具函数
  ├── composables/     # 组合式函数
  └── assets/          # 静态资源
  ```

- **Vue 3 最佳实践**：
  - 优先使用 Composition API（`<script setup>`）
  - 使用 `<script setup lang="ts">`（如果使用 TypeScript）
  - 组件 props 使用 `defineProps`
  - 事件使用 `defineEmits`
  - 响应式数据使用 `ref` 和 `reactive`

#### API 设计规范
- **RESTful 风格**：
  ```
  GET    /api/children          # 获取列表
  GET    /api/children/{id}     # 获取详情
  POST   /api/children          # 创建
  PUT    /api/children/{id}     # 更新
  DELETE /api/children/{id}     # 删除
  ```

- **统一响应格式**：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {},
    "timestamp": 1706140800000
  }
  ```

- **错误码规范**：
  - 200: 成功
  - 400: 请求参数错误
  - 401: 未授权
  - 403: 禁止访问
  - 404: 资源不存在
  - 500: 服务器错误

### 架构模式
- **前后端分离**：通过 RESTful API 通信
- **MVVM 模式**：Vue 前端采用 Model-View-ViewModel
- **三层架构**：后端采用 Controller-Service-Mapper
- **JWT 认证**：无状态认证机制
- **RBAC 权限模型**：基于角色的访问控制

### 测试策略
- **单元测试**：
  - 后端：JUnit 5 + Mockito
  - 前端：Vitest + Vue Test Utils
- **集成测试**：Spring Boot Test
- **API 测试**：Postman/Apifox 自动化测试
- **E2E 测试**：Cypress 或 Playwright

### Git 工作流
- **分支策略**（Git Flow）：
  - `main` - 生产环境分支
  - `develop` - 开发环境分支
  - `feature/*` - 功能分支
  - `fix/*` - bug 修复分支
  - `release/*` - 发布分支

- **提交信息格式**：
  ```
  <type>(<scope>): <subject>

  <body>

  <footer>

  Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
  ```
  - type: feat, fix, docs, style, refactor, test, chore
  - scope: backend, frontend-website, frontend-admin, database
  - subject: 简短描述（中文）

  示例：
  ```
  feat(backend): 添加捐赠记录管理 API

  - 创建 Donation 实体和 DonationController
  - 实现捐赠记录的 CRUD 操作
  - 添加按日期范围查询接口

  Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
  ```

## 领域上下文

### 核心概念
- **留守儿童**：父母外出务工，由祖辈或亲戚监护的儿童
- **受助儿童**：接受救助平台帮助的留守儿童
- **志愿者**：参与救助活动的爱心人士
- **捐赠者**：提供资金或物资捐赠的个人/企业
- **救助项目**：教育支持、心理关爱、生活改善、假期陪伴营等
- **捐赠记录**：每一笔捐赠的详细信息（金额、时间、用途）
- **财务透明**：所有捐赠的流向和使用情况公开可查

### 详细业务流程

#### 流程 1：捐赠流程
```
捐赠者
  ↓ 访问网站
浏览救助项目 → 选择项目 → 选择捐赠金额
  ↓
选择支付方式（微信/支付宝）
  ↓
完成支付 → 生成捐赠记录 → 发送收据（邮件/短信）
  ↓
资金分配到具体项目
  ↓
志愿者执行救助 → 上传活动照片和进展
  ↓
捐赠者查看项目进展和资金使用情况
```

#### 流程 2：受助儿童管理
```
管理员登录后台
  ↓
录入受助儿童信息（姓名、年龄、家庭情况、需求）
  ↓
审核通过 → 儿童档案入库
  ↓
匹配救助项目 → 分配志愿者
  ↓
定期更新儿童状态（学习进展、生活改善）
  ↓
生成季度报告 → 反馈给捐赠者
```

#### 流程 3：志愿者管理
```
用户访问网站 → 注册成为志愿者
  ↓
填写个人信息（技能、可服务时间、意向）
  ↓
后台审核 → 发放志愿者编号
  ↓
系统推送救助活动 → 志愿者报名
  ↓
管理员确认 → 活动执行
  ↓
上传活动照片和服务记录
  ↓
统计志愿时长 → 积分奖励
```

#### 流程 4：财务管理流程
```
捐赠入账 → 记录到系统
  ↓
资金分配（按项目预算）
  ↓
支出记录（购买物资、活动费用）
  ↓
凭证上传（发票、收据）
  ↓
财务审核 → 账目公示
  ↓
生成月度/季度财务报告
```

#### 流程 5：新闻公告发布
```
管理员登录 → 创建新闻/公告
  ↓
编写标题、内容、上传图片
  ↓
设置发布时间和目标受众
  ↓
定时发布 → 推送给关注用户
  ↓
统计阅读量和互动数据
```

### 功能模块清单

#### 前端网站功能
1. **首页模块**
   - 轮播图展示
   - 数据统计展示（受助人数、筹款金额）
   - 最新项目进展
   - 快速捐赠入口

2. **关于我们**
   - 组织介绍
   - 团队成员
   - 荣誉资质

3. **救助项目**
   - 项目列表展示
   - 项目详情（目标、进展、资金使用）
   - 项目捐赠入口

4. **受助故事**
   - 儿童故事展示
   - 成功案例
   - 照片/视频展示

5. **在线捐赠**
   - 捐赠金额选择
   - 支付集成
   - 捐赠记录查询

6. **志愿者招募**
   - 活动列表
   - 报名表单
   - 志愿者故事

7. **透明公示**
   - 财务报告
   - 项目追踪
   - 资金流向可视化

8. **新闻公告**
   - 新闻列表
   - 活动通知
   - 政策公告

9. **用户中心**
   - 个人信息
   - 捐赠记录
   - 志愿服务记录
   - 订阅管理

#### 管理后台功能
1. **仪表板**
   - 数据概览（卡片统计）
   - 捐赠趋势图表
   - 项目进度展示
   - 待办事项提醒

2. **受助儿童管理**
   - 儿童档案 CRUD
   - 批量导入导出
   - 照片上传管理
   - 状态跟踪

3. **志愿者管理**
   - 志愿者信息 CRUD
   - 审核流程
   - 服务时长统计
   - 积分管理

4. **捐赠管理**
   - 捐赠记录查询
   - 退款处理
   - 捐赠收据生成
   - 捐赠统计分析

5. **项目管理**
   - 项目 CRUD
   - 项目预算设置
   - 项目进度跟踪
   - 资金分配

6. **活动管理**
   - 活动发布
   - 报名管理
   - 活动总结
   - 照片/视频上传

7. **新闻公告管理**
   - 内容编辑器（Markdown）
   - 定时发布
   - 分类标签
   - 阅读统计

8. **财务管理**
   - 收支记录
   - 发票管理
   - 财务报表
   - 资金审批流程

9. **用户权限管理**
   - 用户 CRUD
   - 角色权限配置
   - 操作日志
   - 登录日志

10. **系统设置**
    - 基本配置
    - 支付配置
    - 短信/邮件配置
    - 数据备份

## 重要约束

### 技术约束
- **JDK 版本**：Java 17 或 Java 21
- **Spring Boot 版本**：3.x 系列
- **Vue 版本**：Vue 3.x
- **浏览器支持**：Chrome/Firefox/Safari/Edge 最新两个版本
- **响应式要求**：支持 PC（1920px+）和移动（375px+）
- **性能要求**：
  - API 响应时间 < 200ms
  - 首屏加载时间 < 2s
  - 数据库查询 < 100ms

### 业务约束
- **数据隐私**：受助儿童信息脱敏，符合《个人信息保护法》
- **财务透明**：所有捐赠可追溯，资金流向公开
- **法律合规**：符合《慈善法》《网络安全法》等
- **支付安全**：使用官方支付 SDK，不存储敏感支付信息
- **内容审核**：所有发布内容需审核

### 安全要求
- **认证授权**：JWT + Redis 黑名单机制
- **密码加密**：BCrypt 哈希加密
- **SQL 注入防护**：使用参数化查询
- **XSS 防护**：前端输入过滤和转义
- **CSRF 防护**：Token 验证
- **文件上传**：类型白名单 + 大小限制
- **接口限流**：防止恶意请求

### 资源约束
- **开发周期**：一学期（约 16-18 周）
- **团队规模**：1-2 人
- **服务器资源**：阿里云/腾讯云学生机
- **预算限制**：优先使用开源免费工具

## 外部依赖

### 后端依赖
```xml
<!-- 核心框架 -->
<spring-boot.version>3.2.0</spring-boot.version>
<java.version>17</java.version>

<!-- 数据库 -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
</dependency>

<!-- Redis -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

<!-- 安全 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
</dependency>

<!-- 工具类 -->
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
</dependency>

<!-- 文档 -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
</dependency>
```

### 前端依赖
```json
{
  "dependencies": {
    "vue": "^3.4.0",
    "vue-router": "^4.2.0",
    "pinia": "^2.1.0",
    "axios": "^1.6.0",
    "element-plus": "^2.5.0",
    "echarts": "^5.4.0"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^5.0.0",
    "vite": "^5.0.0",
    "sass": "^1.69.0"
  }
}
```

### 数据库配置
- **MySQL 8.0**
  - 地址：localhost:3306
  - 数据库：shourutongxing（从 school 迁移）
  - 用户：root
  - 密码：123456

- **Redis**
  - 地址：localhost:6379
  - 用于：缓存、会话、限流

### 支付服务
- **微信支付**
- **支付宝**

### 消息服务
- **阿里云短信服务**
- **邮件服务**（SMTP）

### 部署平台
- **前端**：Nginx 静态托管
- **后端**：阿里云/腾讯云服务器
- **数据库**：云数据库 MySQL
- **代码仓库**：GitHub
