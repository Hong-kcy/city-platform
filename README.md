# city-platform 城市商业街区虚实融合数字消费平台

围绕真实商业街空间（温州梧田老街 Demo 场景）组织商户、活动、路线和消费行为的平台。
当前处于 **Demo MVP 已验收** 状态，覆盖：Platform、Merchant、Street、User、Activity、ExperienceRoute、Task/Coupon、Recommendation。

## 仓库结构

```text
city-platform/
├── server/    # Spring Boot 3 业务后端（Java 21 / MySQL 8 / MyBatis / Flyway）
├── app/       # 微信小程序用户端（原生小程序）
├── admin/     # Web 商户/运营管理端（Vue 3 + Vite）
├── docs/      # 规范文档（流程.txt / 技术规范.txt / 功能大纲.txt）
├── .gitignore
├── README.md
└── CONTRIBUTING.md
```

职责边界：用户功能 → `app/`，商户/运营 → `admin/`，业务后端 → `server/`，规范文档 → `docs/`，不跨目录放置。

## 快速开始（后端）

前置：JDK 21、Maven、MySQL 8。

1. 创建空数据库（表结构与种子数据由 Flyway 自动执行）：

   ```sql
   CREATE DATABASE city_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. 通过环境变量注入数据库凭证（仓库内只有配置格式，无真实密钥）：

   ```powershell
   $env:DB_PASSWORD="你的密码"
   mvn spring-boot:run
   ```

   可选变量：`DB_HOST`（默认 localhost）、`DB_PORT`（默认 3306）、`DB_NAME`（默认 city_platform）、`DB_USERNAME`（默认 root）。

3. 验证：`http://localhost:8080/api/street-areas/101/map`

数据库 schema 变更规范见 [CONTRIBUTING.md](CONTRIBUTING.md)。

## 快速开始（微信小程序用户端）

1. 微信开发者工具导入 `app/` 目录。
2. 小程序本身不占用端口，通过 `app/config.js` 中的 `API_BASE_URL` 请求后端接口；本机模拟器调试时保持 `http://localhost:8080`（即后端端口），真机调试需改为局域网 IP 并放行后端 8080 端口。

## 快速开始（Web 管理端）

```powershell
cd admin
npm install
npm run dev     # http://localhost:5173，已代理 /api、/uploads、/static 到后端 8080
```

Demo 阶段商户后台为演示身份选择机制（未实现完整 RBAC），见 docs/流程.txt 相关 Scope Decision。

## 环境变量一览

| 变量 | 用途 | 说明 |
|---|---|---|
| `DB_USERNAME` / `DB_PASSWORD` | 数据库凭证 | 必填（无默认密码） |
| `DB_HOST` / `DB_PORT` / `DB_NAME` | 数据库连接 | 默认 localhost:3306/city_platform |
| `WECHAT_MINI_APP_ID` / `WECHAT_MINI_APP_SECRET` | 微信小程序登录（服务端） | 无凭证时登录接口返回明确业务错误 |

## 更多文档

- 开发阶段与验收标准：`docs/流程.txt`
- 架构与编码规范：`docs/技术规范.txt`
- 产品功能大纲：`docs/功能大纲.txt`
