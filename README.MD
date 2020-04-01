# 通用的用户授权体系 基础代码


将 boot-common, boot-redis 剥离出去，可以作为一个独立的jar包给其他工程依赖
那么这个工程就保留：
boot-core
boot-web
只保留基本的增删用户/角色/权限/单位/区域 等基本操作

如果有业务系统的逻辑，那么将业务功能模块写在 boot-web里面。不要写到boot-core里面去
boot-core依旧可以打包给其他模块依赖






