package com.lar.security.user.domain.server;

import com.lar.security.user.domain.repository.jpa.UserEntity;
import com.lar.security.user.domain.repository.jpa.UserRepositoty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//服务层实现,这边就可以使用具体仓库组织代码
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepositoty store;

    @Override
    public UserEntity getUserByUserName(String username) {
        return store.getUserByUserName(username);
    }
}
