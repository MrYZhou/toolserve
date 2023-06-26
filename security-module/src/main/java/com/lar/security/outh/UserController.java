package com.lar.security.outh;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.lar.security.outh.model.UserQuery;
import com.lar.security.outh.model.UserView;
import com.lar.security.outh.repository.UserEntity;
import com.lar.security.outh.server.UserService;
import com.lar.security.outh.util.PasswordUtil;
import com.lar.util.RedisUtil;
import com.lar.vo.AppResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.HashMap;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    RedisUtil redisUtil;
    // 复杂的业务推荐
    @Autowired
    private UserService userService;

    @PostMapping("/info")
    public AppResult<Object> getUser(@RequestBody UserQuery userQuery) {
        return AppResult.success(null);
    }

    // 测试登录，浏览器访问： http://localhost:8081/user/login?username=zhang&password=123456
    @PostMapping("/login")
    public AppResult<Object> doLogin(@RequestBody UserView user) throws SQLException {
        UserEntity userEntity = userService.getUserByUserName(user.getUsername());
        String encryptPassword = PasswordUtil.encode(user.getPassword(), userEntity.getSalt());
        if (encryptPassword.equals(userEntity.getPassword())) {
            StpUtil.login(userEntity.getId());
            String token = StpUtil.getTokenValue();
            HashMap<String, String> map = new HashMap<>();
            map.put("token", token);
            return AppResult.success(map);
        }
        return AppResult.fail("登录失败");
    }

    @PostMapping("/registe")
    public AppResult<Object> registe(@RequestBody UserView user) throws SQLException {

        return AppResult.success();
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @RequestMapping("isLogin")
    public String isLogin() {

        return "当前会话是否登录：" + StpUtil.isLogin();
    }

    // 查询 Token 信息  ---- http://localhost:8081/user/tokenInfo
    @RequestMapping("tokenInfo")
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

    // 测试注销  ---- http://localhost:8081/user/logout
    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        log.info("注销操作");
        return SaResult.ok();
    }

}
