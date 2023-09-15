package com.lar.user.model;

import lombok.Data;

/**
 * 返回前端的实体
 */
@Data
public class UserView {
  private String id;
  private String username;
  private String password;
  private String salt;
}
