package com.lar.security.model;

import lombok.Data;

@Data
public class UserView {
  //  @JSONField(name = "username")
  private String userName;

  private String password;
}
