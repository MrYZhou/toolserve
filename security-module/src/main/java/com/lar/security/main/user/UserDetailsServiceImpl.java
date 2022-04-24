package com.lar.security.main.user;

import com.lar.security.main.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import system.menu.MenuRepository;

import java.util.List;

/** 此类很关键，在security认证时候会去调用获取数据库用户的方法 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired UserService userService;
  @Autowired MenuRepository menuRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = userService.getUserByUsername(username);
    if (user == null) {
      throw new RuntimeException("用户不存在");
    }
    // 查询权限信息存储
    List<String> permissions = menuRepository.selectPermissionById(user.getId());
    return new LoginUser(user, permissions);
  }
}
