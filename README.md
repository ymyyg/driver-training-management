# 驾校培训管理系统

一个面向驾校日常业务的毕业设计项目，提供学员、教练员、车辆、培训记录、系统用户和数据统计等管理能力。项目采用 Spring Boot 构建后端服务，并内置静态管理页面。

## 主要功能

- 学员信息的新增、查询、修改和删除
- 教练员信息管理与条件检索
- 车辆档案及车辆状态管理
- 培训记录管理与条件查询
- 系统用户管理和登录
- 学员、教练员、车辆、培训记录等数据概览统计

## 技术栈

- Java 17、Spring Boot 4、Spring Web MVC
- Spring Data JPA、MySQL 8
- Maven Wrapper、Docker Compose

## 项目结构

```text
src/main/java/com/fzx/drivertrainingmanagement
├─ controller   # HTTP 接口
├─ service      # 业务逻辑
├─ repository   # 数据访问
├─ entity       # 数据实体
└─ common       # 通用返回结构

src/main/resources
├─ static       # 前端静态页面
└─ application.properties
```

## 快速开始

### Docker Compose（推荐）

1. 复制环境变量示例并修改其中的数据库密码：

   ```powershell
   Copy-Item .env.example .env
   ```

2. 构建并启动：

   ```powershell
   docker compose up --build
   ```

3. 浏览器访问 <http://localhost:8080>。

停止服务可运行 `docker compose down`。

### 本地运行

准备 Java 17 和 MySQL 8，并创建数据库：

```sql
CREATE DATABASE driver_training_management
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

在 PowerShell 中配置连接并启动：

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:mysql://localhost:3306/driver_training_management?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8&allowPublicKeyRetrieval=true"
$env:SPRING_DATASOURCE_USERNAME = "root"
$env:SPRING_DATASOURCE_PASSWORD = "你的数据库密码"
.\mvnw.cmd spring-boot:run
```

## 主要接口

| 模块 | 基础路径 | 说明 |
| --- | --- | --- |
| 学员管理 | `/students` | 增删改查、条件查询 |
| 教练员管理 | `/drivers` | 增删改查、条件查询 |
| 车辆管理 | `/vehicles` | 增删改查、条件查询 |
| 培训记录 | `/training-records` | 增删改查、条件查询 |
| 用户管理 | `/users` | 用户管理、查询与登录 |
| 数据统计 | `/statistics/overview` | 系统数据概览 |
| 服务测试 | `/hello` | 简单连通性测试 |

## 配置与安全

- 不要提交 `.env`、真实数据库密码、Token 或其他密钥。
- 仓库仅提供 `.env.example` 作为配置模板。
- 当前登录功能用于毕业设计演示；生产环境应增加密码哈希、身份认证、权限控制和参数校验。

## 构建与测试

```powershell
.\mvnw.cmd test
```

构建可运行包：

```powershell
.\mvnw.cmd clean package
```

## 开源许可

本项目基于 [MIT License](LICENSE) 开源，仅用于学习、交流与毕业设计展示。
