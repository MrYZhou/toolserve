package com.lar.security.main.model;

import com.lar.security.main.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/** 重写默认UserDetails实现 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements UserDetails {
  private UserEntity user;

  private List<String> permissions;

  List<SimpleGrantedAuthority> authorities;

  public LoginUser(UserEntity user, List<String> permissions) {
    this.user = user;
    this.permissions = permissions;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (authorities != null) {
      return authorities;
    }
    List<SimpleGrantedAuthority> authorities =
        permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    return authorities;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUserName();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
