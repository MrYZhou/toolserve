package com.lar.security.oauth;

import cn.dev33.satoken.annotation.SaCheckDisable;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckSafe;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.dev33.satoken.util.SaResult;
import com.lar.common.enums.AppConfig;
import com.lar.common.util.JsonUtil;
import com.lar.common.util.RedisUtil;
import com.lar.common.vo.AppResult;
import com.lar.security.user.model.UserView;
import com.lar.security.user.repository.UserEntity;
import com.lar.security.user.server.UserService;
import com.wf.captcha.SpecCaptcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.data.id.IdUtil;
import org.dromara.hutool.core.util.RandomUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    @Autowired
    RedisUtil redisUtil;
    // 复杂的业务推荐
    @Autowired
    private UserService userService;


    // 验证码:https://github.com/ele-admin/EasyCaptcha
    @ResponseBody
    @RequestMapping("/captcha")
    public AppResult<Object> captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        String verCode = specCaptcha.text().toLowerCase();
        String key = RandomUtil.randomNumbers(10);
        // 存入redis并设置过期时间为30分钟
        redisUtil.setEx(key, verCode, 30, TimeUnit.MINUTES);
        // 将key和base64返回给前端
        return AppResult.success(new HashMap<>() {{
            put("key", key);
            put("image", specCaptcha.toBase64());
        }});
    }

    // 测试登录，浏览器访问： http://localhost:8081/user/login?username=zhang&password=123456
    @PostMapping("/login")
    public AppResult<Object> doLogin(@RequestBody UserView user) throws SQLException {
        // 获取redis中的验证码
        String verCode = user.getVerKey();
        String redisCode = redisUtil.get(verCode);
        // 判断验证码
        if (verCode == null || !redisCode.equals(verCode.trim().toLowerCase())) {
            return AppResult.fail("验证码不正确");
        }
        // 前置校验
        UserEntity userEntity = userService.getUserByUserName(user.getUsername());
        if (userEntity == null) return AppResult.fail("用户不存在");
        String encryptPassword = SaSecureUtil.aesEncrypt(user.getPassword(), AppConfig.SALT);
        if (!encryptPassword.equals(userEntity.getPassword())) {
            return AppResult.fail("密码错误");
        }

        // 主线逻辑, false是不记住我，true是记住我.但是这个主要是必须用的是基于cookie的方式
        // 如果是前后端分离模式主要其实是要给后端token.后端没法做更多
        // uniapp
        // 1.临时记住我。getApp().globalData.satoken = "xxxx-xxxx-xxxx-xxxx-xxx";
        // 2.永久记住 uni.setStorageSync("satoken", "xxxx-xxxx-xxxx-xxxx-xxx");

        // pc
        //1.临时记住我。sessionStorage.setItem("satoken", "xxxx-xxxx-xxxx-xxxx-xxx");
        //2.永久记住.localStorage.setItem("satoken", "xxxx-xxxx-xxxx-xxxx-xxx");

        try {
            StpUtil.login(userEntity.getId(), new SaLoginParameter()
                            .setDeviceType("PC")                 // 此次登录的客户端设备类型, 用于[同端互斥登录]时指定此次登录的设备类型
                            .setIsLastingCookie(true)        // 是否为持久Cookie（临时Cookie在浏览器关闭时会自动删除，持久Cookie在重新打开后依然存在）
                            .setTimeout(60 * 60 * 24 * 7)    // 指定此次登录token的有效期, 单位:秒 （如未指定，自动取全局配置的 timeout 值）
//                .setToken("xxxx-xxxx-xxxx-xxxx") // 预定此次登录的生成的Token
                            .setIsWriteHeader(false)         // 是否在登录后将 Token 写入到响应头
            );
        } catch (Exception e) {
            // -1NotLoginException.NOT_TOKEN未能从请求中读取到Token
            // -2NotLoginException.INVALID_TOKEN已读取到 Token，但是 Token无效
            // -3NotLoginException.TOKEN_TIMEOUT已读取到 Token，但是 Token已经过期
            // -4NotLoginException.BE_REPLACED已读取到 Token，但是 Token 已被顶下线
            // -5NotLoginException.KICK_OUT已读取到 Token，但是 Token 已被踢下线


        }

        String token = StpUtil.getTokenValue();

        // 结果返回的处理
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        return AppResult.success(map);
    }

    @PostMapping("/register")
    public AppResult<?> registe(@RequestBody UserView user) throws SQLException {
        UserEntity entity = JsonUtil.toBean(user, UserEntity.class);
        entity.setPassword(SaSecureUtil.aesEncrypt(entity.getPassword(), AppConfig.SALT));
        entity.setId(IdUtil.getSnowflakeNextIdStr());
        List<UserEntity> users = new ArrayList<>();
        if (users != null && users.size() > 0) {
            return AppResult.fail("账户已注册");
        }
        userService.insertUser(entity);
        return AppResult.success("注册成功");
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @RequestMapping("isLogin")
    //使用 @SaIgnore忽略掉路由拦截认证,可以临时测试用很方便
    @SaIgnore
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
    @SaCheckDisable
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
    @SaCheckLogin
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

    // 删除仓库    ---- http://localhost:8081/safe/deleteProject
    @RequestMapping("deleteProject")
    public SaResult deleteProject(String projectId) {
        // 第1步，先检查当前会话是否已完成二级认证
        // 		这个地方既可以通过 StpUtil.isSafe() 手动判断，
        // 		也可以通过 StpUtil.checkSafe() 或者 @SaCheckSafe 来校验（校验不通过时将抛出 NotSafeException 异常）
        if (!StpUtil.isSafe()) {
            return SaResult.error("仓库删除失败，请完成二级认证后再次访问接口");
        }

        // 第2步，如果已完成二级认证，则开始执行业务逻辑
        // ...

        // 第3步，返回结果
        return SaResult.ok("仓库删除成功");
    }

    // 提供密码进行二级认证    ---- http://localhost:8081/safe/openSafe?password=123456
    @RequestMapping("openSafe")
    public SaResult openSafe(String password) {
        // 比对密码（此处只是举例，真实项目时可拿其它参数进行校验）
        if ("123456".equals(password)) {

            // 比对成功，为当前会话打开二级认证，有效期为120秒，意为在120秒内再调用 deleteProject 接口都无需提供密码
            StpUtil.openSafe(120);
            return SaResult.ok("二级认证成功");
        }

        // 如果密码校验失败，则二级认证也会失败
        return SaResult.error("二级认证失败");
    }

    // 手动关闭二级认证    ---- http://localhost:8081/safe/closeSafe
    @RequestMapping("closeSafe")
    public SaResult closeSafe() {
        StpUtil.closeSafe();
        return SaResult.ok();
    }


    // ------------------ 指定业务类型进行二级认证

    // 获取应用秘钥    ---- http://localhost:8081/safe/getClientSecret
    @RequestMapping("getClientSecret")
    @SaCheckSafe(value = "client")
    public SaResult getClientSecret() {
        // 第1步，先检查当前会话是否已完成 client业务 的二级认证
        StpUtil.checkSafe("client");

        // 第2步，如果已完成二级认证，则返回数据
        return SaResult.data("aaaa-bbbb-cccc-dddd-eeee");
    }

    // 提供手势密码进行二级认证    ---- http://localhost:8081/safe/openClientSafe?gesture=35789
    @RequestMapping("openClientSafe")
    public SaResult openClientSafe(String gesture) {
        // 比对手势密码（此处只是举例，真实项目时可拿其它参数进行校验）
        if ("35789".equals(gesture)) {

            // 比对成功，为当前会话打开二级认证：
            // 业务类型为：client
            // 有效期为600秒==10分钟，意为在10分钟内，调用 getClientSecret 时都无需再提供手势密码
            StpUtil.openSafe("client", 600);
            return SaResult.ok("二级认证成功");
        }

        // 如果密码校验失败，则二级认证也会失败
        return SaResult.error("二级认证失败");
    }

    // 查询当前会话是否已完成指定的二级认证    ---- http://localhost:8081/safe/isClientSafe
    @RequestMapping("isClientSafe")
    public SaResult isClientSafe() {
        return SaResult.ok("当前是否已完成 client 二级认证：" + StpUtil.isSafe("client"));
    }


}
