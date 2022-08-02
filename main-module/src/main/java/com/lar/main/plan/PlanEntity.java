package com.lar.main.plan;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Table(name = "plan")
@Entity
@Data
public class PlanEntity {
    @Id
//    @Column(name = "id")
    @GeneratedValue(generator = "snowflakeId")
    @GenericGenerator(name = "snowflakeId", strategy = "common.config.database.GenerateSnowflakeId")
    @Column(nullable = false, length = 32)
    private String id;
    @Column(name = "name")
    private String name;
}
