package com.lar.main.plan;

import common.base.AppResult;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/plan")
@AllArgsConstructor
public class PlanController {

  private PlanService planService;

  @RequestMapping(value = "/list")
  @PreAuthorize("hasAnyAuthority('test')")
  public AppResult getPlanList() {

    return AppResult.success(planService.findAll());
  }
}
