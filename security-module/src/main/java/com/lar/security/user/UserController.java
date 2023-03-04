package com.lar.security.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.lar.security.model.UserView;
import com.lar.common.base.AppResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {
  @Autowired(required = false) UserService userService;

  @RequestMapping(value = "/list")
  public List list() {
    UserEntity userEntity = new UserEntity();
    //    userService.login(userEntity);
    return null;
  }

  // 测试登录  ---- http://localhost:8081/acc/doLogin?name=zhang&pwd=123456
  @RequestMapping("doLogin")
  public SaResult doLogin(String name, String pwd) {
    // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
    if("zhang".equals(name) && "123456".equals(pwd)) {
      StpUtil.login(10001);
      return SaResult.ok("登录成功");
    }
    return SaResult.error("登录失败");
  }

  // 查询登录状态  ---- http://localhost:8081/acc/isLogin
  @RequestMapping("isLogin")
  public SaResult isLogin() {
    return SaResult.ok("是否登录：" + StpUtil.isLogin());
  }

  // 查询 Token 信息  ---- http://localhost:8081/acc/tokenInfo
  @RequestMapping("tokenInfo")
  public SaResult tokenInfo() {
    return SaResult.data(StpUtil.getTokenInfo());
  }

  // 测试注销  ---- http://localhost:8081/acc/logout
  @RequestMapping("logout")
  public SaResult logout() {
    StpUtil.logout();
    return SaResult.ok();
  }
}
