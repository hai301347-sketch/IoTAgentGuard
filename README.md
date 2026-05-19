# IoT Agent驱动型物联网设备智能监控管理系统

基于 **Vue3 + Spring Boot + MySQL** 的物联网设备智能监控平台，集成 AI Agent 模块，支持设备管理、实时数据监控、智能告警、自然语言指令解析与自动巡检。

## 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | Vue 3 + Vite + Element Plus + ECharts |
| 后端 | Spring Boot 2.7 + MyBatis + JWT |
| 数据库 | MySQL 8.0 |
| AI Agent | 数据模拟 / 告警检测 / NLP解析 / 自动巡检 |

## 系统功能

- **用户认证** — 登录、注册、JWT 鉴权
- **仪表盘** — 设备总数、在线率、告警统计等可视化
- **设备管理** — 设备增删改查、分页筛选
- **实时监控** — 环境数据实时展示与图表
- **历史数据** — 历史环境数据查询与趋势分析
- **告警管理** — 告警记录查看与处理
- **AI Agent** — 智能巡检、异常检测、自然语言控制

## 项目结构

```
IoTAgentGuard/
├── iot-backend/        # Spring Boot 后端
│   ├── src/main/java/  # Java 源码
│   └── sql/            # 数据库初始化脚本
├── iot-frontend/       # Vue3 前端
│   └── src/
└── docs/               # 项目文档
```

## 快速启动

> 详细部署步骤请参考 [部署指南](部署指南.md)

### 后端

```bash
cd iot-backend
# 初始化数据库（先创建 iot_guard 库）
mysql -u root -p iot_guard < sql/init.sql
# 启动后端
mvn spring-boot:run
```

### 前端

```bash
cd iot-frontend
npm install
npm run dev
```

访问 http://localhost:5173，默认账号 `admin` / `admin123`

## 项目文档

| 文档 | 说明 |
|------|------|
| [项目开发文档](项目开发文档.md) | 系统架构、技术选型、目录结构、功能模块说明 |
| [接口文档](接口文档.md) | RESTful API 接口定义与请求/响应示例 |
| [部署指南](部署指南.md) | 环境要求、安装步骤、启动流程 |
| [实训项目报告](实训项目报告.md) | 项目背景、目标、开发过程与总结 |
