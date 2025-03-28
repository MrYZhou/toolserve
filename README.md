# toolserve

## 介绍

学习总结,对常用的问题解决方案试验，此版本为无 module,用文件夹来替代 module 的效果。多 module 在 multi-module 分支.考虑到简单项目整什么多 module，实际使用依赖非常混乱

## 模块说明

1.common-module 基础包，常见的工具包和业务底层依赖添加，系统其他模块依赖此模块

2.main-module 业务模块,写业务使用。

3.security-module 认证授权模块,为了避免后续微服务时候重启系统模块会出现要在登录。同时考虑后续 sso 等  
登录功能比较复杂。 其次也是比较稳定的功能。故拆分

4.system-module 系统模块，是系统功能的集合。如菜单，角色，组织管理等内在的功能。由于功能属于核心的  
功能，基本不会怎么变化。 故拆分

## 环境需安装

1.jdk 版本 17+

2.maven 版本 3.5+

3.redis 强依赖

4.mysql5.7+

使用

启动 MainModuleApplication 类

## docker 构建

先用 maven 插件的 package 打包下

方式 1:使用分层构建  
docker build -t toolserve .  
方式 2:使用全量构建  
docker build -t toolserve -f ./dockerfile2 .

运行  
docker run -p 8081:8081 toolserve
