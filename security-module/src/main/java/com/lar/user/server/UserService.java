package com.lar.user.server;


import com.lar.user.repository.UserEntity;

import java.sql.SQLException;

//对复杂应用可以使用，如调用超过4个服务或仓库领域,否则这一层可以空置
public interface UserService {
    UserEntity getUserByUserName(String username);

    Long insertUser(UserEntity userEntity) throws SQLException;
}
