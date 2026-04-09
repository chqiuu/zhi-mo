# 执墨 - AI 新媒体内容创作平台

> 执笔倚墨，AI成文

## 项目简介

执墨是一款 AI 驱动的新媒体内容创作工具，专注于 AI + 教育赛道，帮助创作者快速完成：爆款对标 → 提示词生成 → AI 创作 → 一键发布 的全流程。

## 定位

- **差异化**：专注教育赛道
- **核心价值**：让内容创作更高效

## 核心功能

| 模块 | 功能 | 优先级 |
|------|------|--------|
| 🔥 爆款对标 | 低粉爆款文章列表、赛道筛选 | P1 |
| ✍️ 提示词生成 | 人设模板库、素材分析、Prompt 生成 | P1 |
| 🤖 AI创作 | 输入标题→生成文章+配图 | P0 |
| 📝 在线编辑器 | HTML导入、可视化编辑、实时预览 | P0 |
| 🚀 一键复制 | 复制标题/摘要/正文到公众号 | P0 |
| 📊 素材库 | 收藏、整理素材 | P2 |

## 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | Vue3 + Element Plus + Tiptap |
| 后端 | Spring Boot + MyBatis Plus |
| 数据库 | MySQL + Redis |
| 部署 | Docker Compose |

## 项目结构

```
zhi-mo/
├── backend/                    # Spring Boot 后端 (端口 10000)
│   └── src/main/java/com/chqiuu/zhi-mo/
│       ├── api/               # API 接口
│       │   └── article/       # 文章模块
│       ├── common/            # 公共组件
│       └── config/            # 配置类
├── frontend/                   # Vue3 前端 (端口 10001)
│   └── src/
│       ├── views/            # 页面
│       ├── components/       # 组件
│       └── api/               # API 调用
├── docs/                      # 项目文档
├── docker-compose.yml          # Docker 配置
└── README.md
```

## 快速启动

### 1. 启动依赖服务

```bash
docker compose up -d
```

### 2. 初始化数据库

```bash
docker exec -i zhi-mo-mysql mysql -uroot -proot123456 < backend/src/main/resources/db/init.sql
```

### 3. 启动后端

```bash
cd backend
mvn spring-boot:run
```

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
```

## 访问地址

| 服务 | 地址 |
|------|------|
| 前端界面 | http://localhost:10001 |
| 后端 API | http://localhost:10000/zhi-mo/api |
| API 文档 | http://localhost:10001/swagger-ui.html |

## nginx 配置

参考 `docs/nginx配置参考.md`，将配置添加到 OrbStack nginx 容器：

```
/zhi-mo/api/ → http://localhost:10000
/zhi-mo/     → http://localhost:10001
```

## 开发指南

### 添加新模块

1. 后端：在 `backend/src/main/java/com/chqiuu/zhi-mo/api/` 下创建模块目录
2. 前端：在 `frontend/src/views/` 下创建页面组件
3. 数据库：更新 `backend/src/main/resources/db/init.sql`

### 前端组件开发

使用 Element Plus 组件库，参考：[https://element-plus.org](https://element-plus.org)

### API 调用

在 `frontend/src/api/index.js` 中添加新的 API 方法。

## 相关文档

- [项目分析报告](./docs/项目分析报告.md)
- [功能规划-在线文章编辑器](./docs/功能规划-在线文章编辑器-2026-04-05.md)
- [竞品分析-爆了么案例研究](./docs/竞品分析-爆了么案例研究-2026-04-04.md)

## License

MIT

---

*维护：飞飞（AI助手）+ 老大（开发者）*
