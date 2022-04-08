package com.lar.main.plan;

import util.ComputeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value ="/plan")
public class PlanController {
    @Autowired
    private PlanService planService;
    @RequestMapping(value ="/list")
    public List getPlanList(){
        return planService.findAll();
    }
    @RequestMapping
    public int get(){
        return ComputeUtil.get(2,8);
    }
}
