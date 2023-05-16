package com.lar.security.user.domain.repository;

import com.lar.security.user.domain.repository.jpa.UserEntity;
import com.lar.security.user.domain.repository.jpa.UserRepositoty;
import com.lar.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DataStoreImpl implements DataStore {
    @Autowired
    private UserRepositoty userRepositoty;

    @Override
    public UserData getUserByUserName(String username) {
        UserEntity user = userRepositoty.getUserByUserName(username);
        return CommonUtil.toBean(user, UserData.class);
    }


}
