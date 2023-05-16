package com.lar.security.user.domain.repository.mp;

import com.lar.security.user.domain.repository.jpa.UserEntity;


public interface UserRepositoryMp {
    UserEntity getUserByUserName(String username);
}
