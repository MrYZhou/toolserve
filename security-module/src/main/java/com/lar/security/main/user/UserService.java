package com.lar.security.main.user;

import common.base.AppResult;

/** 用户相关接口 */
public interface UserService {

  LoginUserDetail login(UserEntity user);

  UserEntity getUserByUsername(String username);

  AppResult logout();
}
