# toolserve

#### 介绍

学习总结,对常用的问题解决方案试验

####  

模块说明
1.common-module 基础包，常见的工具包和业务底层依赖添加，系统其他模块依赖次模块
2.main-module 业务模块,写业务使用。
3.security-module 认证授权，考虑到有sso，多租户可能会比较复杂，且个性化较多，故分离出来。
4.system-module 系统模板，组织系统的菜单，用户，角色，组织管理等内在的功能

安装：
1.jdk版本17
maven版本3.5+

使用：
启动MainModuleApplication类
