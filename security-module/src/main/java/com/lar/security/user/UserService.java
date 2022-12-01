package com.lar.security.user;

import com.lar.security.model.UserView;
import common.base.AppResult;

/** 用户相关接口 */
public interface UserService {

  AppResult login(UserView user);

  UserEntity getUserByUsername(String username);

  AppResult logout();
}
