package com.lar.security.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoty extends JpaRepository<UserEntity, String> {
  UserEntity getUserByUserName(String username);
}
