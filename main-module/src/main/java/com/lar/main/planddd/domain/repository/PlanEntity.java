package com.lar.main.planddd.domain.repository;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//jpa业务表
@Table(name = "plan")
@Entity
@Data
public class PlanEntity {
    @Id
    @Column(name = "id")
    private String id;


    // 可以定义改变数据变化的函数
}
