package com.lar.user.server;

import com.lar.user.repository.UserEntity;
import org.noear.wood.DbContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

//服务层实现,这边就可以使用具体仓库组织代码
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private DbContext dbContext;

    @Override
    public UserEntity getUserByUserName(String username) {
        try {
            return dbContext.sql("select * from sys_user where username=?", username).getItem(UserEntity.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
