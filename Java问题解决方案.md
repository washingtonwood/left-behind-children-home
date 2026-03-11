# Java 版本兼容性问题 - 完整解决方案

## 🔍 问题分析

### 错误信息
```
Fatal error compiling: java.lang.ExceptionInInitializerError
com.sun.tools.javac.code.TypeTag :: UNKNOWN
```

### 根本原因
- 系统安装：Java 25.0.1（最新版本）
- 项目配置：Java 17
- Maven 编译器插件：不支持使用 Java 25 编译 Java 17 代码

## ✅ 解决方案

### 方案1：安装 Java 21 LTS（推荐）⭐

#### 步骤1：下载 Java 21
1. 访问：https://adoptium.net/temurin/releases/?version=21
2. 选择：
   - Version: 21 (LTS)
   - Operating System: Windows
   - Architecture: x64
   - Package Type: JDK
   - Download Type: .msi Installer

#### 步骤2：安装 Java 21
1. 运行下载的 .msi 安装程序
2. 使用默认安装路径：`C:\Program Files\Eclipse Adoptium\jdk-21`
3. 完成安装

#### 步骤3：启动后端
双击运行 `启动后端-Java21.bat`

---

### 方案2：使用便携版 Java 21

#### 下载便携版
```cmd
# 使用 PowerShell 下载
cd C:\Program Files\Eclipse Adoptium
Invoke-WebRequest -Uri "https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.3%2B9/OpenJDK21U-jdk_x64_windows_hotspot_21.0.3_9.zip" -OutFile "jdk-21.zip"
Expand-Archive -Path jdk-21.zip -DestinationPath .
```

---

### 方案3：修改启动脚本使用 Java 21

创建文件 `启动后端-Java21.bat`：
```batch
@echo off
chcp 65001 >nul
cd /d D:\bi\shouhutongxing-backend

REM 使用 Java 21（假设已安装）
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo 使用 Java 版本:
java -version
echo.

echo 启动后端服务...
call mvnw.cmd clean spring-boot:run
pause
```

---

### 方案4：多版本 Java 共存（高级）

#### 使用 jEnv 或 SDKMAN 管理多个 Java 版本

Windows 环境变量配置：
```
JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21
PATH=%JAVA_HOME%\bin;...
```

---

## 🚀 快速启动（安装 Java 21 后）

### 创建启动脚本
保存为 `启动后端-Java21.bat`：
```batch
@echo off
chcp 65001 >nul
cls
echo ========================================
echo 守护童行 - 后端服务（Java 21）
echo ========================================
echo.

cd /d "%~dp0shouhutongxing-backend"

REM 指定使用 Java 21
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo [1/3] Java 环境...
java -version
echo.

echo [2/3] MySQL 服务...
netstat -ano | findstr :3306 >nul
if errorlevel 1 (
    echo ❌ MySQL 未运行
    pause
    exit /b 1
)
echo ✅ MySQL 正常
echo.

echo [3/3] 启动后端...
call mvnw.cmd clean spring-boot:run
pause
```

---

## 📋 验证安装

运行以下命令验证 Java 21：
```cmd
"C:\Program Files\Eclipse Adoptium\jdk-21\bin\java.exe" -version
```

应该看到：
```
java version "21.0.x" or openjdk version "21.0.x"
```

---

## 🛠️ 故障排除

### 问题1：找不到 Java 21
**解决**：确认安装路径是否正确
```cmd
dir "C:\Program Files\Eclipse Adoptium\"
```

### 问题2：编译仍然失败
**解决**：清理 Maven 缓存
```cmd
cd D:\bi\shouhutongxing-backend
rmdir /s /q target
mvnw.cmd clean
```

### 问题3：环境变量冲突
**解决**：在脚本中显式设置 JAVA_HOME
```batch
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"
```

---

## 📝 下载链接

| Java 版本 | 下载链接 |
|----------|---------|
| Java 21 LTS | https://adoptium.net/temurin/releases/?version=21 |
| Java 17 LTS | https://adoptium.net/temurin/releases/?version=17 |
| Adoptium 官网 | https://adoptium.net/ |

---

## ⚡ 快速命令

### 检查已安装的 Java 版本
```cmd
dir "C:\Program Files\Eclipse Adoptium\" /b
```

### 测试 Java 21
```cmd
"C:\Program Files\Eclipse Adoptium\jdk-21\bin\java.exe" -version
```

### 启动后端（使用 Java 21）
```cmd
cd D:\bi\shouhutongxing-backend
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"
mvnw.cmd spring-boot:run
```

---

## 🎯 推荐流程

1. **下载 Java 21 LTS**（稳定版本）
2. **安装到默认路径**
3. **运行** `启动后端-Java21.bat`
4. **测试** http://localhost:8080/api

---

需要帮助？请访问：
- [Adoptium 文档](https://adoptium.net/docs)
- [Spring Boot 文档](https://spring.io/projects/spring-boot)
