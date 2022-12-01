package com.lar.xlj.model;

import lombok.Data;

import java.util.List;

@Data
public class ResultDream {
    private String id;
    private String title;
    private String des;
    private List<String> list;
}
