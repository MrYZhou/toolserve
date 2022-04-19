package com.lar.security.main.user;

import common.base.AppResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired private UserRepositoty userRepositoty;

  @Override
  public LoginUserDetail login(UserEntity user) {
    // 查询用户信息
    UserEntity entity = this.getUserByUsername(user.getUserName());

    if (entity != null) {
      return new LoginUserDetail(entity);
    }

    return null;
  }

  @Override
  public UserEntity getUserByUsername(String username) {
    return userRepositoty.getUserByUserName(username);
  }

  @Override
  public AppResult logout() {
    return null;
  }
}
