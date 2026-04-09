#!/bin/bash

# 执墨 - 启动脚本

echo "🚀 启动执墨项目..."

# 1. 启动 Docker 服务 (MySQL + Redis)
echo "📦 启动 Docker 服务..."
docker compose up -d

# 2. 等待 MySQL 启动
echo "⏳ 等待 MySQL 启动..."
sleep 10

# 3. 初始化数据库
echo "🗄️ 初始化数据库..."
docker exec -i zhi-mo-mysql mysql -uroot -proot123456 < backend/src/main/resources/db/init.sql

# 4. 启动后端
echo "🔧 启动后端 (端口 10000)..."
cd backend && mvn spring-boot:run &

# 5. 启动前端
echo "🎨 启动前端 (端口 10001)..."
cd frontend && npm install && npm run dev &

echo "✅ 启动完成!"
echo "📝 后端 API: http://localhost:10000/zhi-mo/api"
echo "📝 前端界面: http://localhost:10001"
