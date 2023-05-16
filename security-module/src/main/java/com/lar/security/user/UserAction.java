package com.lar.security.user;

import com.lar.security.user.domain.repository.UserData;
import com.lar.security.user.domain.server.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//可以认为服务编排的类，用于展示业务服务调用流
@Service
public class UserAction {
    @Autowired
    private UserService userService;

    public UserData getUserByUserName(String name) {
        // 第一步获取组装仓库的数据

        // 主业务 (操作属性变化的业务）

        // 其他支线

        return userService.getUserByUserName(name);
    }


}
