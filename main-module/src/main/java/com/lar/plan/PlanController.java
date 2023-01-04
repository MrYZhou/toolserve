package com.lar.plan;

import com.lar.plan.model.PlanQuery;
import com.lar.common.base.AppResult;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/plan")
@AllArgsConstructor
public class PlanController {

    @Autowired
    private PlanService planService;
    @Autowired
    private PlanRepositoty planReposity;

    @RequestMapping("/list")
    @PreAuthorize("hasAnyAuthority('test')")
    public AppResult getPlanList() {
        return AppResult.success(planService.findAll());
    }

    @GetMapping("/add")
    public AppResult add() {
        PlanEntity planEntity = new PlanEntity();
        planEntity.setName("一");
        return AppResult.success(planReposity.save(planEntity));
    }

    @PostMapping("/page")
    public AppResult page(@RequestBody PlanQuery query) {
        Page<PlanEntity> data = planService.page(query);
        HashMap<String, Object> map = new HashMap<>();
        map.put("list", data.getContent());
        map.put("total", data.getTotalElements());
        return AppResult.success(map);
    }
}
