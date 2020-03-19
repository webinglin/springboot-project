# !/bin/bash
######################################################################################
# 功能: 1、将Jenkins编译后的spider工程三个项目复制到/usr/local/deploy_tools/update目录下
#       2、在本地上新版本与指定的旧版本差异化比较，生成增量以及delete.sh
#       3、打包生成增量升级包
#	    4、由于存在主干、分支不同，需要确认以下两个参数：PROJECT_PATH，CODE_PATH
# 说明：./diff_package_local.sh <init_version> <final_version>
# 例子：./diff_package_local.sh V3.1.02402 V3.1.02501
######################################################################################
# Copyright (c) 2012-2022 厦门市美亚柏科信息股份有限公司
# History version information:
# [chenkun][20170626]



# 初始变量设置
# /usr/local/deploy_build/
# /usr/local/deploy_build/bootJar/
# /usr/local/deploy_tool/
# /usr/local/deploy_tool/update/


function init_config()
{
	#前一版本
	INIT_VERSION=$1
	#后一版本
	FINAL_VERSION=$2
	#增量包输出目录：如update_V3.1.02501
	INC_PATH="inc_$2"
	#打包目录
	TAR_PATH=/usr/local/deploy_tool

	# 判断是否已经变异过了，如果变异过了，那么就直接退出，不在继续执行
	if [ -d /usr/local/deploy_tool/update/$2 ]
		then
		echo "${FINAL_VERSION}版本目录已经存在，如果要重新编译，请手动删除已经编译的结果";
		exit 1;
	fi	
}


function usage_diff()
{
	echo '使用方法：'
	echo '         sh diff_local.sh 参数1(旧版本) 参数2(新版本)'
	echo '      例：'
	echo '         sh diff_local.sh V3.5.03101 V3.5.04901'
	exit 1 
}

function compile_project()
{
	sh build.sh
	[ -d $TAR_PATH/update/${FINAL_VERSION}/ ] || mkdir -p $TAR_PATH/update/${FINAL_VERSION}/
	rm -rf $TAR_PATH/update/${FINAL_VERSION}/*
	cp -r /usr/local/deploy_build/portals-web/build/libs/* $TAR_PATH/update/${FINAL_VERSION}/

}

function run_diff_file()
{
	echo "开始导出差异化文件..."
	rm ${TAR_PATH}/${INC_PATH} -rf 
	mkdir -p ${TAR_PATH}/${INC_PATH}
	#diff -ruNa $FINAL_VERSION $INIT_VERSION |grep 'diff -ruNa' >/root/tmp.txt
	diff -rNq ${TAR_PATH}/update/$FINAL_VERSION ${TAR_PATH}/update/$INIT_VERSION >/root/tmp.txt
	while read line
		do
		final=`echo $line |awk -F ' and ' '{print $1}'|awk -F '^Files ' '{print $2}'`
		init=`echo $line |awk -F ' and ' '{print $2}'|awk -F ' differ$' '{print $1}' `
		if [ -f "$init" ] && [ -f "$final" ]
			then
			cp --parents -p "$final" ${TAR_PATH}/${INC_PATH}
		elif [ ! -f "$init" ] && [  -f "$final" ]
			then
			cp --parents -p "$final" ${TAR_PATH}/${INC_PATH}
		else [  -f "$init" ] && [ ! -f "$final" ]
			FINAL_VERSION1=`echo $FINAL_VERSION | sed "s/\/$//g"`
			o=`echo $final|sed  "s/^.*${FINAL_VERSION1}//"`
			echo "rm -f /u01/res${o}" >> ${TAR_PATH}/${INC_PATH}/deleteNginx.sh
			echo "rm -f /usr/local/PortalWeb1${o}" >> ${TAR_PATH}/${INC_PATH}/deleteWeb1.sh
			echo "rm -f /usr/local/PortalWeb2${o}" >> ${TAR_PATH}/${INC_PATH}/deleteWeb2.sh
		fi
	done</root/tmp.txt

	mv ${TAR_PATH}/${INC_PATH}/usr/local/deploy_tool/update/$FINAL_VERSION/* ${TAR_PATH}/${INC_PATH}/
	rm ${TAR_PATH}/${INC_PATH}/usr -rf
	rm /root/tmp.txt -f
}


function export_sqlScript() 
{
	echo "脚本请从svn确认并下载最新脚本"
#	echo "开始导出sqlScript..."
#	[ -d ${TAR_PATH}/${INC_PATH}/sqlScript ] || mkdir -p ${TAR_PATH}/${INC_PATH}/sqlScript
#	svn export -q --force $CODE_PATH/doc/sqlScript/update/$POST_VERSION ${TAR_PATH}/${INC_PATH}/sqlScript
#	svn export -q --force $CODE_PATH/doc/sqlScript/update/svn_update.sql ${TAR_PATH}/${INC_PATH}/sqlScript/svn_update.sql
}


function zip_diff_file()
{
	echo "开始压缩差异包"
	zip -qr "$INC_PATH.zip" "$INC_PATH"
	rm -fr $INC_PATH
	echo "打包完成，请导出最新的sql脚本"
}



#程序入口:
#检查参数
if [ ! $# -eq 2 ]; then 
	#输入参数不对时显示使用方法
	usage_diff
else 
	#初始化变量
	init_config $1 $2
fi

# 拷贝编译结果
compile_project
#执行比较并输出到相应文件
run_diff_file
#下载sql脚本
export_sqlScript
#打包
zip_diff_file
