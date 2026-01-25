# 项目上下文

## 目的
守护童行 - 留守儿童救助平台是一个面向公益组织的综合管理系统，旨在：
- 为留守儿童提供教育支持、心理辅导和生活资助
- 实现善款筹集、项目追踪和透明公示
- 提供受助儿童信息管理、志愿者管理和捐赠管理功能
- 通过数字化平台实时更新项目进展，让爱心看得见

## 技术栈

### 前端
- **HTML5** - 页面结构
- **Tailwind CSS** - 通过 CDN 引入的实用优先 CSS 框架
- **Google Fonts** - Noto Sans SC 和 Noto Serif SC 中文字体
- **原生 JavaScript** - 客户端交互逻辑

### 后端（计划中）
- **Node.js** 或 **Python** - 待确定
- **RESTful API** - 前后端分离架构

### 数据库
- **MySQL 8.0** - 数据存储
  - school 数据库（现有）
  - 包含表：student, teacher, member, social, new, notification, user

### 部署
- **GitHub Pages** - 静态页面托管
- 仓库地址：https://github.com/washingtonwood/left-behind-children-home

## 项目约定

### 代码风格
- **HTML**：
  - 使用语义化标签
  - Tailwind 类名顺序：布局 → 间距 → 尺寸 → 颜色 → 其他
  - 自定义 CSS 放在 `<style>` 标签中，位于 `</head>` 前

- **CSS**：
  - 优先使用 Tailwind 实用类
  - 自定义样式使用清晰描述的类名（如 `.card-hover`）
  - 颜色使用 Tailwind 色标（primary, accent 等）

- **JavaScript**：
  - 使用 ES6+ 语法
  - 函数命名使用驼峰命名法（camelCase）
  - 常量使用大写下划线（UPPER_SNAKE_CASE）

### 架构模式
- **前后端分离**：前端通过 API 与后端通信
- **单页面应用（SPA）趋势**：考虑后续迁移到 SPA 框架
- **响应式设计**：移动优先（mobile-first）设计原则

### 测试策略
- **手动测试**：当前阶段通过浏览器手动测试
- **未来计划**：引入自动化测试（Jest, Playwright）

### Git 工作流
- **分支策略**：
  - `main` - 主分支，保持稳定可部署状态
  - `feature/*` - 功能开发分支
  - `fix/*` - bug 修复分支

- **提交信息格式**：
  ```
  <type>: <description>

  Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
  ```
  - type: add, update, fix, refactor, docs
  - 使用中文描述

- **代码审查**：合并到 main 前需要审查

## 领域上下文

### 核心概念
- **留守儿童**：父母外出务工，由祖辈或亲戚监护的儿童
- **受助儿童**：接受救助平台帮助的留守儿童
- **志愿者**：参与救助活动的爱心人士
- **捐助**：来自个人或企业的资金和物资支持
- **救助项目**：教育支持、心理关爱、生活改善、假期陪伴营等

### 业务流程
1. 捐赠者在线捐赠
2. 平台记录捐赠信息
3. 资金分配到具体救助项目
4. 志愿者执行救助活动
5. 实时反馈救助进展
6. 定期发布财务报告和项目进展

## 重要约束

### 技术约束
- **浏览器兼容性**：支持现代浏览器（Chrome, Firefox, Safari, Edge 最新两个版本）
- **响应式要求**：必须在移动设备（320px+）和桌面设备上良好显示
- **性能要求**：首屏加载时间 < 3 秒

### 业务约束
- **数据隐私**：受助儿童个人信息需要脱敏处理
- **财务透明**：所有捐赠必须可追溯
- **法律合规**：符合《慈善法》等相关法规

### 资源约束
- **当前阶段**：无专职后端开发人员
- **预算限制**：优先使用免费/开源工具和服务

## 外部依赖

### CDN 服务
- **Tailwind CSS**：https://cdn.tailwindcss.com
- **Google Fonts**：https://fonts.googleapis.com

### 数据库
- **MySQL 8.0**：本地安装，端口 3306
  - 用户：root
  - 密码：123456
  - 数据库：school

### 部署平台
- **GitHub Pages**：静态托管
- **GitHub**：代码仓库和版本控制

### 未来计划集成
- **支付网关**：微信支付、支付宝
- **短信服务**：用于通知发送
- **邮件服务**：用于捐赠收据和项目进展通知
