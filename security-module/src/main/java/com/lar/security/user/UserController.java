package com.lar.security.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.lar.security.user.model.UserLogin;
import com.lar.security.user.model.UserQuery;
import com.lar.security.user.repository.UserRepositoty;
import com.lar.security.user.server.UserService;
import com.lar.util.RedisUtil;
import com.lar.vo.AppResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/user" )
@Slf4j
public class UserController {
    @Autowired
    RedisUtil redisUtil;
    // 复杂的业务推荐
    @Autowired
    private UserService userService;
    // 简单的业务推荐
    @Autowired
    private UserRepositoty userRepositoty;

    @PostMapping("/info" )
    public AppResult<Object> getUser(@RequestBody UserQuery userQuery) {
        return AppResult.success(userRepositoty.getUserByUserName(userQuery.getName()));
    }

    // 测试登录，浏览器访问： http://localhost:8081/user/login?username=zhang&password=123456
    @PostMapping("/login" )
    public AppResult<Object> doLogin(@RequestBody UserLogin user) {
        // 从数据库拿
        String encrptPassword = user.getPassword();
        if ("zhang".equals(user.getUsername()) && "123456".equals(user.getPassword())) {
            StpUtil.login(10001);
            String token = StpUtil.getTokenValue();
            HashMap<String, String> map = new HashMap<>();
            map.put("token", token);
            return AppResult.success(map);
        }
        return AppResult.fail("登录失败" );
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @RequestMapping("isLogin" )
    public String isLogin() {

        return "当前会话是否登录：" + StpUtil.isLogin();
    }

    // 查询 Token 信息  ---- http://localhost:8081/user/tokenInfo
    @RequestMapping("tokenInfo" )
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

    // 测试注销  ---- http://localhost:8081/user/logout
    @RequestMapping("logout" )
    public SaResult logout() {
        StpUtil.logout();
        log.info("注销操作");
        return SaResult.ok();
    }

}
