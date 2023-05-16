package com.lar.security.user;

import com.lar.security.user.domain.repository.jpa.UserRepositoty;
import com.lar.security.user.model.UserQuery;
import com.lar.vo.AppResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user" )
public class UserController {
    // 复杂的业务推荐
    @Autowired
    private UserAction userServiceAction;
    // 简单的业务推荐
    @Autowired
    private UserRepositoty userRepositoty;

    @RequestMapping("/getUser" )
    public AppResult<Object> getUser(@RequestBody UserQuery userQuery) {

        return AppResult.success(userRepositoty.getUserByUserName(userQuery.getName()));
    }
}
