package com.lar.security.main.user;

import com.lar.security.main.model.UserView;
import common.base.AppResult;

public interface UserService {

  AppResult login(UserView user);

  UserEntity getUserByUsername(String username);

  AppResult logout();
}
