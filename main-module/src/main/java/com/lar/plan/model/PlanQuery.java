package com.lar.plan.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlanQuery {

    private LocalDateTime startTime;
    //    @JsonProperty("page_index")
    
    private Integer page;
    private Integer size;
}
