package com.lar.oauth;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.lar.enums.AppConfig;
import com.lar.user.model.UserQuery;
import com.lar.user.model.UserView;
import com.lar.user.repository.UserEntity;
import com.lar.user.server.UserService;
import com.lar.util.JsonUtil;
import com.lar.util.PasswordUtil;
import com.lar.util.RedisUtil;
import com.lar.vo.AppResult;
import lombok.extern.slf4j.Slf4j;
import org.noear.wood.DbContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/user" )
@Slf4j
public class AuthController {
    @Autowired
    RedisUtil redisUtil;
    // 复杂的业务推荐
    @Autowired
    private UserService userService;
    @Autowired
    private DbContext db;

    @PostMapping("/info" )
    public AppResult<Object> getUser(@RequestBody UserQuery userQuery) {
        return AppResult.success(null);
    }

    // 测试登录，浏览器访问： http://localhost:8081/user/login?username=zhang&password=123456
    @PostMapping("/login" )
    public AppResult<Object> doLogin(@RequestBody UserView user) throws SQLException {
        UserEntity userEntity = userService.getUserByUserName(user.getUsername());
        String encryptPassword = PasswordUtil.encode(user.getPassword(), userEntity.getSalt());
        if (encryptPassword.equals(userEntity.getPassword())) {
            StpUtil.login(userEntity.getId());
            String token = StpUtil.getTokenValue();
            HashMap<String, String> map = new HashMap<>();
            map.put("token" , token);
            return AppResult.success(map);
        }
        return AppResult.fail("登录失败" );
    }

    @PostMapping("/register" )
    public AppResult<?> registe(@RequestBody UserView user) throws SQLException {
        UserEntity entity = JsonUtil.toBean(user, UserEntity.class);
        entity.setPassword(PasswordUtil.encode(entity.getPassword(), AppConfig.SALT));
        entity.setId(IdUtil.getSnowflakeNextIdStr());
        List<UserEntity> users = db.mapperBase(UserEntity.class).selectByMap(new HashMap<>() {{
            put("username", user.getUsername());
        }});
        if(users!=null && users.size()>0){
            return AppResult.fail("账户已注册");
        }
        userService.insertUser(entity);
        return AppResult.success("注册成功");
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
        StpUtil.logout(10001);                    // 强制指定账号注销下线
        StpUtil.logout(10001, "PC" );              // 强制指定账号指定端注销下线
        StpUtil.logoutByTokenValue("token" );      // 强制指定 Token 注销下线

        log.info("注销操作" );
        return SaResult.ok();
    }

    /**
     * 强制注销 和 踢人下线 的区别在于：
     * 强制注销等价于对方主动调用了注销方法，再次访问会提示：Token无效。
     * 踢人下线不会清除Token信息，而是将其打上特定标记，再次访问会提示：Token已被踢下线。
     *
     * @return
     */
    @RequestMapping("kickout" )
    public SaResult kickout() {
        StpUtil.kickout(10001);                    // 将指定账号踢下线
        StpUtil.kickout(10001, "PC" );              // 将指定账号指定端踢下线
        StpUtil.kickoutByTokenValue("token" );      // 将指定 Token 踢下线


        log.info("注销操作" );
        return SaResult.ok();
    }

}
