package com.lar.main.plan;

import com.lar.main.plan.model.PlanQuery;
import common.base.AppResult;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/plan")
@AllArgsConstructor
public class PlanController {

    private PlanService planService;

    @RequestMapping("/list")
    @PreAuthorize("hasAnyAuthority('test')")
    public AppResult getPlanList() {
        return AppResult.success(planService.findAll());
    }

    @GetMapping("/page")
    public AppResult page(@RequestBody PlanQuery query) {
        Page<PlanEntity> data = planService.page(query);
        return AppResult.success(data);
    }
}
