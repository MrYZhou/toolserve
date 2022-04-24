package com.lar.security.main.user;

import com.lar.security.main.model.UserView;
import common.base.AppResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {
  @Autowired UserService userService;

  @RequestMapping(value = "/list")
  public List list() {
    UserEntity userEntity = new UserEntity();
    //    userService.login(userEntity);
    return null;
  }

  @PostMapping(value = "/login")
  public AppResult login(@RequestBody UserView userView) {

    return userService.login(userView);
  }

  @GetMapping(value = "/logout")
  public AppResult logout() {

    return userService.logout();
  }
}
