## 开箱即用的体系
包含： 区域管理、单位管理、用户管理、角色管理、应用管理（权限管理）、日志记录、配置管理、应用分类管理、访问控制 等基础通用的功能
(去掉子系统管理、)


## Spring-Boot gradle-plugin 相关说明

Spring-Boot 项目以 gradle插件的方式启动说明

1、IDEA工程运行项目的时候，要以包含main方法的启动类的工程的 bootRun 方式启动，这样才能够访问到 src/main/webapps/ 目录下的静态资源。不能直接运行SpringBoot的启动类

2、SpringBoot 默认的静态资源配置为 /static 或者 /public 所以要将静态资源提取到和jar包同目录的 /static 目录下。这样，JSP文件和JS、CSS文件才可以被访问到



## 安装说明
1、执行 bootJar 任务，会再 build/libs/ 目录生成
> lib/		-- 依赖的JAR包
>		
> static/	-- 包含JSP、JS、CSS 。src/main/webapps 目录下的文件全部拷贝到这个目录
> 
> boot-web-1.0.jar  -- 可执行JAR包
> 
> config			-- 项目的配置文件存放位置，application.yml 或者 logback.xml 方便动态改变工程参数
>
> boot-web-1.0.conf	-- 配置文件，设置JVM的启动参数、编码等
> 

2、将生成的/libs 目录下的所有文件，全部拷贝到 linux 服务器上的某一个路径。
> chmod +x boot-web-1.0.jar 给jar包授予 可执行的权限
> 
> ln -s /usr/local/boot-web/boot-web-1.0.jar  /etc/init.d/BootWeb
> 	-- 创建软链到 /etc/init.d/ 目录，形成一个独立的服务。 这样就可以直接通过 service BootWeb start
> 和 service BootWeb stop 来启动服务和停止服务了
>

3、将 boot-web-1.0.conf 配置文件放在Linux服务器上的 boot-web-1.0.jar 同级目录下

4、在 boot-web-1.0.jar的同级目录下创建 config/ 目录可以用来存放 application.yml 和 logback.xml 这样可以比较方便的修改项目的配置项




## 项目自动化打包
通过 /usr/local/deploy_tool/ 目录下的 diff_local.sh 脚本来实现自动化构建，生成差异包或者全量包



