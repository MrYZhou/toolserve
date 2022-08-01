package com.lar.main.planddd.domain.server;


import com.lar.main.planddd.domain.repository.PlanRepositoty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//服务层实现,这边就可以使用具体仓库组织代码
@Service
public class PlanServiceImpl implements PlanService {

    @Autowired
    private PlanRepositoty planReposity;

    @Override
    public List findAll() {

        List<String> ids = null;

        return planReposity.findAll();
    }
}
