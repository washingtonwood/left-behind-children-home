# 资金捐赠系统 - 完整启动指南

## 前置条件检查清单

### 1. 检查 MySQL 服务状态

```bash
# Windows 检查 MySQL 服务
sc query MySQL80
```

如果没有安装或启动MySQL，请：
- 下载安装 MySQL 8.0+
- 启动 MySQL 服务：`net start MySQL80`

### 2. 创建数据库

**方法一：使用命令行**
```bash
mysql -u root -p < check-db.sql
```

**方法二：使用 Navicat/MySQL Workbench**
1. 连接到 MySQL（用户名: root，密码: 123456）
2. 执行 `check-db.sql` 文件中的 SQL 语句

### 3. 启动后端服务

```bash
cd D:\bi\shouhutongxing-backend
mvn spring-boot:run
```

等待看到类似输出：
```
Started ShouhutongxingApplication in X seconds
```

### 4. 测试 API

打开浏览器访问：
- http://localhost:8080/api/fund-donations/statistics

应该看到 JSON 响应：
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "EDUCATION": {...},
    "MEDICAL": {...},
    "LIVING": {...}
  }
}
```

### 5. 测试前端页面

访问：http://localhost:3000/fund-donation.html

## 常见问题排查

### 问题1：提交失败，请检查网络连接

**检查步骤：**
1. 按 F12 打开浏览器开发者工具
2. 切换到 Console 选项卡
3. 查看是否有错误信息

**可能原因：**
- 后端服务未启动（端口8080）
- 数据库未连接
- CORS 跨域问题

### 问题2：数据库连接失败

**解决方法：**
1. 确保 MySQL 服务正在运行
2. 检查 application.properties 中的数据库配置：
   - 用户名：root
   - 密码：123456
   - 数据库名：school
3. 确认数据库 `school` 已创建

### 问题3：端口被占用

**检查并释放端口：**
```bash
# 查看占用8080端口的进程
netstat -ano | findstr 8080

# 结束进程（替换 PID 为实际进程ID）
taskkill /F /PID xxxxx
```

## 快速测试脚本

创建文件 `test-fund-api.bat`：

```batch
@echo off
echo 测试资金捐赠 API...
echo.

echo 1. 测试项目统计接口...
curl -X GET http://localhost:8080/api/fund-donations/statistics
echo.

echo 2. 测试排行榜接口...
curl -X GET http://localhost:8080/api/fund-donations/leaderboard/monthly
echo.

echo 3. 测试感恩墙接口...
curl -X GET http://localhost:8080/api/fund-donations/gratitude-wall
echo.

pause
```

## 验证清单

- [ ] MySQL 服务正在运行
- [ ] 数据库 `school` 已创建
- [ ] 数据表 `fund_donation` 已创建
- [ ] 测试数据已插入
- [ ] 后端服务启动成功（端口8080）
- [ ] API 返回正确数据
- [ ] 前端页面可以访问
- [ ] 提交捐赠表单成功

## 开发环境配置总结

- **前端端口**: 3000（Python HTTP Server）
- **后端端口**: 8080（Spring Boot）
- **数据库**: MySQL 3306
- **前端**: http://localhost:3000/fund-donation.html
- **后端 API**: http://localhost:8080/api/fund-donations
