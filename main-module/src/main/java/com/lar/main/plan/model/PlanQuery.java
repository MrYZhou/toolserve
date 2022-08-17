package com.lar.main.plan.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlanQuery {

    //    private Date startTime;
    LocalDateTime startTime = LocalDateTime.of(2000, 1, 1, 1, 1);
    private Integer page;
    private Integer size;
}
