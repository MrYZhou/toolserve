package com.lar.main.plan;

import common.base.AppResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/plan")
public class PlanController {
  @Autowired private PlanService planService;

  @RequestMapping(value = "/list")
  @PreAuthorize("hasAnyAuthority('test')")
  public AppResult getPlanList() {

    return AppResult.success(planService.findAll());
  }
}
