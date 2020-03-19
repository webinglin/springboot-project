#!/bin/bash

# 1. 更新Mysql
# 2. 备份代码
# 3. 清空work目录的缓存
# 4.1 更新脚本

service PortalWeb2 stop

# 删除资源
sh codes/deleteWeb2.sh


# 4.2 更新代码
# 拷贝代码到 PortalWeb2目录

cp -rf codes/static /usr/local/PortalWeb2/
cp -rf codes/lib /usr/local/PortalWeb2/
cp -rf codes/portals-web-1.0-SNAPSHOT.jar /usr/local/PortalWeb2/
chmod +x /usr/local/PortalWeb2/portals-web-1.0-SNAPSHOT.jar

# cp -rf codes/portals-web-1.0-SNAPSHOT.conf /usr/local/PortalWeb2/ 
# cp -rf web2/config /usr/local/PortalWeb2/

# 建立软链接->注册为服务
# rm -f /etc/init.d/PortalWeb2
# ln -s /usr/local/PortalWeb2/portals-web-1.0-SNAPSHOT.jar  /etc/init.d/PortalWeb2


# 更新application.yml
#source  /root/portal_soft/config.properties
#\cp application2.yml /usr/local/PortalWeb2/config/application.yml
#sed -i "s/172.16.32.114/${IP}/g"  /usr/local/PortalWeb2/config/application.yml

sed -i 's/31457280/629145600/g' /usr/local/PortalWeb2/config/application.yml 
rm -rf /usr/local/PortalWeb2/static/WEB-INF/jsp/tools/


service PortalWeb2 start

echo "upgrade code done ."
echo "CONGRATULATION! UPGRADE CODES SUCESS!"
