package com.lar.security.user;

import cn.hutool.jwt.JWTUtil;
import com.lar.security.model.LoginUser;
import com.lar.security.model.UserView;
import common.base.AppResult;
import common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {

  @Autowired private RedisUtil redisUtil;
  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private UserRepositoty userRepositoty;

  @Override
  public AppResult login(UserView user) {

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
    //    使用authenticationManager认证
    Authentication authenticate = authenticationManager.authenticate(authenticationToken);
    if (authenticate == null) {
      return AppResult.fail("用户名或密码错误");
    }
    LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
    String id = loginUser.getUser().getId();
    HashMap<String, Object> map =
        new HashMap<>() {
          {
            put("uid", id);
          }
        };
    String token = JWTUtil.createToken(map, "secret".getBytes());
    // 保存在redis中
    redisUtil.setObject("login" + id, loginUser);
    return AppResult.success("登录成功", token);
  }

  @Override
  public UserEntity getUserByUsername(String username) {
    return userRepositoty.getUserByUserName(username);
  }

  @Override
  public AppResult logout() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    LoginUser principal = (LoginUser) authentication.getPrincipal();
    String id = principal.getUser().getId();
    redisUtil.delete("login" + id);
    return AppResult.success("登出成功");
  }
}
