package com.lar.security.user.domain.repository;


public interface DataStore {
    UserData getUserByUserName(String username);
}
