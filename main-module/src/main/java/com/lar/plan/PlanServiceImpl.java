package com.lar.plan;

import com.lar.plan.model.PlanQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {

    @Autowired
    private PlanRepositoty planReposity;

    @Override
    public List findAll() {

        List<String> ids = null;

        return planReposity.findAll();
    }

    @Override
    public Page<PlanEntity> page(PlanQuery query) {

        Page<PlanEntity> all = planReposity.findAll(PageRequest.of(query.getPage(), query.getSize()));
        return all;
    }
}
