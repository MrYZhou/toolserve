package com.lar.security.main.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {
  @Autowired UserService userService;

  @RequestMapping(value = "/list")
  public List login() {
    UserEntity userEntity = new UserEntity();
    userService.login(userEntity);
    return null;
  }

  @RequestMapping(value = "/sync")
  public List sync() {
    //    UserEntity userEntity = new UserEntity();
    //    userService.login(userEntity);
    return null;
  }
}
