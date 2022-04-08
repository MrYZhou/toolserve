package com.lar.main.plan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService{

    @Autowired
    private PlanRepositoty planReposity;
    @Override
    public List findAll() {

        List<String> ids = null;

        return planReposity.findAll();
    }
}
