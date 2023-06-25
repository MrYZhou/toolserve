package com.lar.security.outh.server;

import com.lar.security.outh.repository.UserEntity;
import org.springframework.stereotype.Service;

//服务层实现,这边就可以使用具体仓库组织代码
@Service
public class UserServiceImpl implements UserService {


    @Override
    public UserEntity getUserByUserName(String username) {
        return null;
    }
}
