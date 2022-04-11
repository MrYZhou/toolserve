package com.lar.main.plan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import common.util.ComputeUtil;


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



}
