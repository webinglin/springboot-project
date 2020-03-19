#!/bin/bash


# 停止服务
service PortalWeb1 stop
# 删除资源
sh codes/deleteWeb1.sh
# 4.2 更新代码
# 拷贝代码到 PortalWeb1目录

cp -rf codes/static /usr/local/PortalWeb1/
cp -rf codes/lib /usr/local/PortalWeb1/
cp -rf codes/portals-web-1.0-SNAPSHOT.jar /usr/local/PortalWeb1/
chmod +x /usr/local/PortalWeb1/portals-web-1.0-SNAPSHOT.jar

# cp -rf codes/portals-web-1.0-SNAPSHOT.conf /usr/local/PortalWeb1/
# cp -rf web1/config /usr/local/PortalWeb1/

# 建立软链接->注册为服务
# rm -f /etc/init.d/PortalWeb1
# ln -s /usr/local/PortalWeb1/portals-web-1.0-SNAPSHOT.jar  /etc/init.d/PortalWeb1

# 更新application.yml
#source  /root/portal_soft/config.properties
#\cp application1.yml /usr/local/PortalWeb1/config/application.yml
#sed -i "s/172.16.32.114/${IP}/g"  /usr/local/PortalWeb1/config/application.yml

sed -i 's/31457280/629145600/g' /usr/local/PortalWeb1/config/application.yml 
rm -rf /usr/local/PortalWeb1/static/WEB-INF/jsp/tools/

service PortalWeb1 start

echo "upgrade code done ."
echo "CONGRATULATION! UPGRADE CODES SUCESS!"
