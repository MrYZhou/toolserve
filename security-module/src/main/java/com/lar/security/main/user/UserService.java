package com.lar.security.main.user;

import common.base.AppResult;

public interface UserService {

  LoginUserDetail login(UserEntity user);

  UserEntity getUserByUsername(String username);

  AppResult logout();
}
