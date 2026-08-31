# CONTRIBUTING.md 贡献指南

## 分层架构（不可违反）

```text
Controller → Application → Domain → Repository Interface → Infrastructure
```

- Domain 层禁止：查询数据库、调第三方 SDK、调 HTTP、操作事务、依赖 Application / Infrastructure / 其他业务 Domain 实现。
- Application 层负责：跨域协调、聚合 ReadModel、管理事务（`@Transactional`）。
- 跨域引用采用 ID + Application 层校验，不建立 Domain 间物理耦合。
- Controller 只返回 ReadModel / DTO，禁止直接返回 Entity。
- 统一异常体系：`BusinessException` / `NotFoundException` / `IllegalStatusTransitionException` + `GlobalExceptionHandler`，不新建第二套。
- 详细规范见 `docs/技术规范.txt`。

## 数据库变更（Flyway）

- Migration 位于 `server/src/main/resources/db/migration/`：
  - `V1__init_schema.sql` 建表
  - `V2__seed_data.sql` 种子数据（淮河路步行街 101 段 Demo 数据）
- **后续 schema 变更一律新增 `V3__xxx.sql`，禁止修改已发布的 migration 文件**，避免多人开发各自本地手改数据库。
- 种子数据保持幂等（`INSERT IGNORE`），固定使用 101 段 ID；业务测试数据请勿写入 migration。
- 唯一性/状态一致性通过数据库约束 + Application 校验双重保证；跨域 ID 引用不加物理外键（设计取舍，见技术规范第十二章）。
- 本地重置演示数据：DROP DATABASE → CREATE DATABASE → 启动应用由 Flyway 重建。

## 敏感信息

- 仓库内只保留配置格式（环境变量占位），严禁提交真实密码 / AppSecret / Token。
- 本机真实凭证备份在根目录 `1/`（已被 .gitignore 排除）。
- AppSecret 只从服务端环境变量 `WECHAT_MINI_APP_SECRET` 读取，绝不下发前端或硬编码。

## 目录职责

| 目录 | 职责 |
|---|---|
| `app/` | 微信小程序用户端（wx.setStorageSync 存 Token，不使用 localStorage） |
| `admin/` | 商户 + 运营 Web 管理端（merchant/ 与 operation/ 模块分离） |
| `server/` | Spring Boot 业务后端 |
| `docs/` | 规范文档 |

不跨目录放置代码；`server/target/`、`admin/dist/`、`uploads/` 运行产物不入库。

## 提交规范

- 格式：`type: 简述`，type 常用 `feat` / `fix` / `chore` / `docs` / `refactor`。
- 不提交 `server/target/`、运行时上传文件、IDE 配置、`app/project.private.config.json`。

## Demo 阶段明确的 Scope Decision（当前不实现）

- 商户后台完整 RBAC / 身份认证（现为演示身份选择机制，产品化前必须补齐）。
- 第三方地图 SDK（高德/百度）、GeoHash、空间索引、实时热力。
- Redis / MQ / 微服务 / BFF。
- 3DGS、全景、Dynamic Overlay 等第二阶段能力。

以上为阶段性取舍而非遗漏，详见 `docs/流程.txt`。
