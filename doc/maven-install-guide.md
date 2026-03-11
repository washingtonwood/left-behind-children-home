# Maven 安装指南（Windows 手动安装）

## 方法一：使用 Chocolatey（如果有）

如果你安装了 Chocolatey：
```bash
choco install maven
```

## 方法二：手动下载安装（推荐）

### 1. 下载 Maven

1. 访问：https://maven.apache.org/download.cgi
2. 下载：`apache-maven-3.9.9-bin.zip`（或最新版本）
3. 解压到：`C:\Program Files\Apache\Maven`

### 2. 配置环境变量

1. **打开系统环境变量：**
   - 按 `Win + X`，选择"系统"
   - 点击"高级系统设置"
   - 点击"环境变量"

2. **新建系统变量 `MAVEN_HOME`：**
   - 变量名：`MAVEN_HOME`
   - 变量值：`C:\Program Files\Apache\Maven`

3. **编辑系统变量 `Path`：**
   - 找到 `Path` 变量，点击"编辑"
   - 点击"新建"，添加：`%MAVEN_HOME%\bin`

### 3. 验证安装

**打开新的命令提示符窗口**，运行：
```bash
mvn -version
```

应该显示：
```
Apache Maven 3.9.x
Maven home: C:\Program Files\Apache\Maven
Java version: 17.0.x
```

---

## 方法三：使用 Spring Boot CLI（最简单）

如果你只是想创建 Spring Boot 项目，其实**不需要单独安装 Maven**！

Spring Boot 项目内置了 Maven Wrapper（`mvnw`），会自动下载 Maven。

**直接告诉我 "跳过 Maven 安装"，我就会创建带 Maven Wrapper 的 Spring Boot 项目！**
