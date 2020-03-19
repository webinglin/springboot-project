#!/bin/bash

# 更新Nginx

rm -rf /u01/res/static/WEB-INF/jsp/tools/
sh codes/deleteNginx.sh

cp -rf codes/static /u01/res/

# 更新配置文件
#\cp /usr/local/nginx/conf/nginx.conf /usr/local/nginx/conf/nginx.conf.v1.1.02101
#source  /root/portal_soft/config.properties
#\cp nginx.conf /usr/local/nginx/conf/
#sed -i "s/172.16.32.114/${IP}/g"  /usr/local/nginx/conf/nginx.conf
#/usr/local/nginx/sbin/nginx -s reload

echo "CONGRATULATION! UPGRADE NGINX SUCESS!"
