# Java 17 安装指南（Windows）

## 推荐方案：Eclipse Temurin 17（OpenJDK）

Eclipse Temurin 是开源的 OpenJDK 发行版，免费且稳定。

### 方法一：使用 winget 安装（推荐，最快）

1. **打开 PowerShell 或命令提示符（管理员权限）**

2. **运行以下命令：**
   ```bash
   winget install EclipseAdoptium.Temurin.17.JDK
   ```

3. **验证安装：**
   ```bash
   java -version
   ```
   应该显示类似：`openjdk version "17.0.x"`

### 方法二：手动下载安装

1. **访问下载页面：**
   https://adoptium.net/temurin/releases/?version=17

2. **选择：**
   - 操作系统：Windows
   - 架构：x64（大多数电脑）
   - 类型：JDK
   - 版本：17.0.x（LTS）

3. **下载 `.msi` 安装包**

4. **运行安装程序：**
   - 双击下载的 `.msi` 文件
   - 按照向导完成安装
   - 记住安装路径（默认：`C:\Program Files\Eclipse Adoptium\jdk-17.x.x.x-hotspot\`）

### 方法三：使用 Chocolatey（如果已安装）

```bash
choco install temurin17
```

---

## 配置环境变量

### 1. 设置 JAVA_HOME

1. **打开系统环境变量：**
   - 右键"此电脑" → "属性"
   - "高级系统设置" → "环境变量"

2. **新建系统变量：**
   - 变量名：`JAVA_HOME`
   - 变量值：`C:\Program Files\Eclipse Adoptium\jdk-17.x.x.x-hotspot\`（根据实际安装路径）

### 2. 更新 Path

在系统变量 `Path` 中：
- 删除旧的 Java 8 路径（如有）
- 添加：`%JAVA_HOME%\bin`

### 3. 验证安装

**打开新的命令提示符窗口**，运行：
```bash
java -version
javac -version
```

应该显示：
```
java version "17.0.x"
javac 17.0.x
```

---

## Maven 安装（如果还没有）

Spring Boot 项目需要 Maven 来构建。

### 使用 winget 安装：
```bash
winget install Apache.Maven
```

### 或手动下载：
1. 访问：https://maven.apache.org/download.cgi
2. 下载 `apache-maven-3.9.x-bin.zip`
3. 解压到 `C:\Program Files\Apache\Maven`
4. 配置环境变量：
   - `MAVEN_HOME` = `C:\Program Files\Apache\Maven`
   - Path 添加 `%MAVEN_HOME%\bin`

### 验证 Maven：
```bash
mvn -version
```

---

## 安装后请告诉我

安装完成后，请运行以下命令并告诉我结果：
```bash
java -version
mvn -version
```

然后我会帮你创建 Spring Boot 项目！
