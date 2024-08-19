# toolserve

#### 介绍

学习总结,对常用的问题解决方案试验，此版本为无module,用文件夹来替代module的效果。多module在multi-module分支.
考虑到简单项目整什么多module

####  

模块说明

1.common-module 基础包，常见的工具包和业务底层依赖添加，系统其他模块依赖此模块

2.main-module 业务模块,写业务使用。

3.security-module 认证授权模块,为了避免后续微服务时候重启系统模块会出现要在登录。同时考虑后续sso等
登录功能比较复杂。 其次也是比较稳定的功能。故拆分

4.system-module 系统模块，是系统功能的集合。如菜单，角色，组织管理等内在的功能。由于功能属于核心的
功能，基本不会怎么变化。 故拆分


环境需安装

1.jdk版本17+

2.maven版本3.5+

3.redis 强依赖

4.mysql5.7+

使用

启动MainModuleApplication类



构建
docker build -t toolserve  .
docker build -t toolserve -f ./dockerfile2 .

docker run -p 8081:8081 toolserve