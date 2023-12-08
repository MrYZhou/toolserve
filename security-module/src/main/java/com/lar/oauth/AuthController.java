package com.lar.oauth;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.IdUtil;
import com.lar.enums.AppConfig;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class AuthController {
    @Autowired
    RedisUtil redisUtil;
    // 复杂的业务推荐
    @Autowired
    private UserService userService;
    @Autowired
    private DbContext db;


    // 测试登录，浏览器访问： http://localhost:8081/user/login?username=zhang&password=123456
    @PostMapping("/login")
    public AppResult<Object> doLogin(@RequestBody UserView user) throws SQLException {
        UserEntity userEntity = userService.getUserByUserName(user.getUsername());
        String encryptPassword = PasswordUtil.encode(user.getPassword(), AppConfig.SALT);
        if (encryptPassword.equals(userEntity.getPassword())) {
            StpUtil.login(userEntity.getId());
            String token = StpUtil.getTokenValue();
            HashMap<String, String> map = new HashMap<>();
            map.put("token", token);
            return AppResult.success(map);
        }
        return AppResult.fail("登录失败");
    }

    @PostMapping("/register")
    public AppResult<?> registe(@RequestBody UserView user) throws SQLException {
        UserEntity entity = JsonUtil.toBean(user, UserEntity.class);
        entity.setPassword(PasswordUtil.encode(entity.getPassword(), AppConfig.SALT));
        entity.setId(IdUtil.getSnowflakeNextIdStr());
        List<UserEntity> users = db.mapperBase(UserEntity.class).selectByMap(new HashMap<>() {{
            put("username", user.getUsername());
        }});
        if (users != null && users.size() > 0) {
            return AppResult.fail("账户已注册");
        }
        userService.insertUser(entity);
        return AppResult.success("注册成功");
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
//        StpUtil.logout(StpUtil.getLoginId());                    // 强制指定账号注销下线
//        StpUtil.logout(StpUtil.getLoginId(), "PC" );              // 强制指定账号指定端注销下线
//        StpUtil.logoutByTokenValue("token" );      // 强制指定 Token 注销下线

        return SaResult.ok();
    }

    /**
     * 强制注销 和 踢人下线 的区别在于：
     * 强制注销等价于对方主动调用了注销方法，再次访问会提示：Token无效。
     * 踢人下线不会清除Token信息，而是将其打上特定标记，再次访问会提示：Token已被踢下线。
     *
     * @return
     */
    @RequestMapping("kickout")
    public SaResult kickout() {
        StpUtil.kickout(10001);                    // 将指定账号踢下线
        StpUtil.kickout(10001, "PC");              // 将指定账号指定端踢下线
        StpUtil.kickoutByTokenValue("token");      // 将指定 Token 踢下线


        log.info("注销操作");
        return SaResult.ok();
    }

    /**
     * 权限api校验
     *
     * @return
     */
    @GetMapping("allPermission")
    public SaResult allPermission() {

        // 获取：当前账号所拥有的权限集合
        StpUtil.getPermissionList();

        // 判断：当前账号是否含有指定权限, 返回 true 或 false
        StpUtil.hasPermission("user.add");

        // 校验：当前账号是否含有指定权限, 如果验证未通过，则抛出异常: NotPermissionException
        StpUtil.checkPermission("user.add");

        // 校验：当前账号是否含有指定权限 [指定多个，必须全部验证通过]
        StpUtil.checkPermissionAnd("user.add", "user.delete", "user.get");

        // 校验：当前账号是否含有指定权限 [指定多个，只要其一验证通过即可]
        StpUtil.checkPermissionOr("user.add", "user.delete", "user.get");
        return SaResult.ok();
    }

    /**
     * 角色校验
     *
     * @return
     */

    @GetMapping("allRole")
    public SaResult allRole() {

        // 获取：当前账号所拥有的角色集合
        StpUtil.getRoleList();

        // 判断：当前账号是否拥有指定角色, 返回 true 或 false
        StpUtil.hasRole("super-admin");

        // 校验：当前账号是否含有指定角色标识, 如果验证未通过，则抛出异常: NotRoleException
        StpUtil.checkRole("super-admin");

        // 校验：当前账号是否含有指定角色标识 [指定多个，必须全部验证通过]
        StpUtil.checkRoleAnd("super-admin", "shop-admin");

        // 校验：当前账号是否含有指定角色标识 [指定多个，只要其一验证通过即可]
        StpUtil.checkRoleOr("super-admin", "shop-admin");

        return SaResult.ok();
    }

    // 查询权限   ---- http://localhost:8081/jur/getPermission
    @RequestMapping("getPermission")
    public SaResult getPermission() {
        // 查询权限信息 ，如果当前会话未登录，会返回一个空集合
        List<String> permissionList = StpUtil.getPermissionList();
        System.out.println("当前登录账号拥有的所有权限：" + permissionList);

        // 查询角色信息 ，如果当前会话未登录，会返回一个空集合
        List<String> roleList = StpUtil.getRoleList();
        System.out.println("当前登录账号拥有的所有角色：" + roleList);

        // 返回给前端
        return SaResult.ok()
                .set("roleList", roleList)
                .set("permissionList", permissionList);
    }

    // 权限校验  ---- http://localhost:8081/jur/checkPermission
    @RequestMapping("checkPermission")
    public SaResult checkPermission() {

        // 判断：当前账号是否拥有一个权限，返回 true 或 false
        // 		如果当前账号未登录，则永远返回 false
        StpUtil.hasPermission("user.add");
        StpUtil.hasPermissionAnd("user.add", "user.delete", "user.get");  // 指定多个，必须全部拥有才会返回 true
        StpUtil.hasPermissionOr("user.add", "user.delete", "user.get");     // 指定多个，只要拥有一个就会返回 true

        // 校验：当前账号是否拥有一个权限，校验不通过时会抛出 `NotPermissionException` 异常
        // 		如果当前账号未登录，则永远校验失败
        StpUtil.checkPermission("user.add");
        StpUtil.checkPermissionAnd("user.add", "user.delete", "user.get");  // 指定多个，必须全部拥有才会校验通过
        StpUtil.checkPermissionOr("user.add", "user.delete", "user.get");  // 指定多个，只要拥有一个就会校验通过

        return SaResult.ok();
    }

    // 角色校验  ---- http://localhost:8081/jur/checkRole
    @RequestMapping("checkRole")
    public SaResult checkRole() {

        // 判断：当前账号是否拥有一个角色，返回 true 或 false
        // 		如果当前账号未登录，则永远返回 false
        StpUtil.hasRole("admin");
        StpUtil.hasRoleAnd("admin", "ceo", "cfo");  // 指定多个，必须全部拥有才会返回 true
        StpUtil.hasRoleOr("admin", "ceo", "cfo");      // 指定多个，只要拥有一个就会返回 true

        // 校验：当前账号是否拥有一个角色，校验不通过时会抛出 `NotRoleException` 异常
        // 		如果当前账号未登录，则永远校验失败
        StpUtil.checkRole("admin");
        StpUtil.checkRoleAnd("admin", "ceo", "cfo");  // 指定多个，必须全部拥有才会校验通过
        StpUtil.checkRoleOr("admin", "ceo", "cfo");  // 指定多个，只要拥有一个就会校验通过

        return SaResult.ok();
    }

    // 权限通配符  ---- http://localhost:8081/jur/wildcardPermission
    @RequestMapping("wildcardPermission")
    public SaResult wildcardPermission() {

        // 前提条件：在 StpInterface 实现类中，为账号返回了 "art.*" 泛权限
        StpUtil.hasPermission("art.add");  // 返回 true
        StpUtil.hasPermission("art.delete");  // 返回 true
        StpUtil.hasPermission("goods.add");  // 返回 false，因为前缀不符合

        // * 符合可以出现在任意位置，比如权限码的开头，当账号拥有 "*.delete" 时
        StpUtil.hasPermission("goods.add");        // false
        StpUtil.hasPermission("goods.delete");     // true
        StpUtil.hasPermission("art.delete");      // true

        // 也可以出现在权限码的中间，比如当账号拥有 "shop.*.user" 时
        StpUtil.hasPermission("shop.add.user");  // true
        StpUtil.hasPermission("shop.delete.user");  // true
        StpUtil.hasPermission("shop.delete.goods");  // false，因为后缀不符合

        // 注意点：
        // 1、上帝权限：当一个账号拥有 "*" 权限时，他可以验证通过任何权限码
        // 2、角色校验也可以加 * ，指定泛角色，例如： "*.admin"，暂不赘述

        return SaResult.ok();
    }

}
