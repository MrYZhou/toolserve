package com.lar.security.user.server;

import com.lar.security.user.repository.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

//服务层实现,这边就可以使用具体仓库组织代码
@Service
public class UserServiceImpl implements UserService {




    @Override
    public UserEntity getUserByUserName(String username) {
        return null;
    }
    @Override
    public Long insertUser(UserEntity userEntity) {

        return null;
    }
}
