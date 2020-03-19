# 初始变量设置
# /usr/local/deploy_build	
# /usr/local/deploy_tool

svnurl=http://172.16.1.105/svn/CT-1610/01%E7%BC%96%E8%BE%91%E5%8C%BA/03%E7%BC%96%E7%A0%81/01%E8%BD%AF%E4%BB%B6/trunk/portals-parent
# svnurl=http://172.16.1.105/svn/CT-1610/01编辑区/03编码/01软件/branch/portals-parent-v1.1.01301p04-sz
# svnurl=http://172.16.1.105/svn/CT-1610/01%E7%BC%96%E8%BE%91%E5%8C%BA/03%E7%BC%96%E7%A0%81/01%E8%BD%AF%E4%BB%B6/tag/portal_v1.1.00401_547_jiangsu
reversion=HEAD
# reversion=547


# 1. 环境准备[清空原来目录下的所有文件，重新下载构建]
source /etc/profile

rm -rf /usr/local/deploy_build
mkdir /usr/local/deploy_build
cd /usr/local/deploy_build
rm -rf *


# 2. 下载代码到当前路径
svn checkout ${svnurl} -r ${reversion} . 

# 3. 构建代码
cd portals-web
gradle bootJar

# 4. build/libs/* 这个就是编译的结果

# 5. 压缩代码为zip包格式[带上svn号]
# mkdir /usr/local/deploy_build/bootJar/
# cd /usr/local/deploy_build/bootJar/
# cp -r /usr/local/deploy_build/portals-web/build/libs/* ./

# svn info ${svnurl} > ./svn.log
# str1=`grep "Last Changed Rev:" ./svn.log`
# var_code=`echo ${str1:17}`

# zip -r ./portal_${var_code}.zip /usr/local/deploy_build/portals-web/build/libs/*

echo "CONGRATULATION! BUILD SUCCESS ^-^ "

