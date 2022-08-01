package com.lar.main.planddd.domain.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

//实现类来具体实现
@Repository
public class PlanJpaRepositotyImpl implements PlanRepositoty {

    @Autowired
    JpaRepositoty jpaRepositoty;

    @Override
    public List findAll() {
        return jpaRepositoty.findAll();
    }
}
