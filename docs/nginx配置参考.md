# zhi-mo nginx 配置参考
# 将以下配置添加到 OrbStack nginx 容器的 nginx.conf 中

# 后端 API 代理
location /zhi-mo/api/ {
    proxy_pass http://host.docker.internal:10000/zhi-mo/api/;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
}

# 前端静态资源
location /zhi-mo/ {
    proxy_pass http://host.docker.internal:10001/;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
}
