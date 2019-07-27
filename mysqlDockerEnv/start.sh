#!/usr/bin/env bash
cur_dir='pwd'
docker stop imooc-mysql
docker rm imooc-mysql
docker run --name imooc-mysql -v conf:/etc/mysql/conf.d -v data:/var/lib/mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=aA111111 -d mysql:latest