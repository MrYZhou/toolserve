package com.lar.security.user.model;

import lombok.Data;

/**
 * 返回前端的实体
 */
@Data
public class UserLogin {
  private String username;
  private String password;
}
