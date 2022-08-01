package com.lar.main.planddd;

import common.base.AppResult;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/planddd")
@AllArgsConstructor
public class PlanController {

    private PlanServiceAction planServiceAction;

    @RequestMapping("/list")
    @PreAuthorize("hasAnyAuthority('test')")
    public AppResult getPlanList() {
        return AppResult.success(planServiceAction.findAll());
    }
}
