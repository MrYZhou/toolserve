package com.lar.security.user.domain.repository.mp;

import com.lar.security.user.domain.repository.jpa.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepositoryMp {
    @Override
    public UserEntity getUserByUserName(String username) {
        return null;
    }
}
