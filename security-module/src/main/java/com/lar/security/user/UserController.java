package com.lar.security.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.lar.security.user.domain.repository.jpa.UserRepositoty;
import com.lar.security.user.model.UserLogin;
import com.lar.security.user.model.UserQuery;
import com.lar.util.RedisMan;
import com.lar.vo.AppResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user" )
@Slf4j
public class UserController {
    @Autowired
    RedisMan redisMan;
    // 复杂的业务推荐
    @Autowired
    private UserAction userServiceAction;
    // 简单的业务推荐
    @Autowired
    private UserRepositoty userRepositoty;

    @PostMapping("/info" )
    public AppResult<Object> getUser(@RequestBody UserQuery userQuery) {
        return AppResult.success(userRepositoty.getUserByUserName(userQuery.getName()));
    }

    // 测试登录，浏览器访问： http://localhost:8081/user/login?username=zhang&password=123456
    @PostMapping("/login" )
    public String doLogin(@RequestBody UserLogin user) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对

        String encrptPassword = user.getPassword();
        if ("zhang".equals(user.getUsername()) && "123456".equals(user.getPassword())) {
            StpUtil.login(10001);
            return "登录成功";
        }
        return "登录失败";
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @RequestMapping("isLogin" )
    public String isLogin() {
        String s = redisMan.get("11" );

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
