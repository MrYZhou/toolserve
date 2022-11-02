package com.lar.main.xlj.model;

import lombok.Data;

import java.util.List;

@Data
public class JsonRootBeanDream {
    private String reason;
    private List<ResultDream> result;
    private int error_code;
}
