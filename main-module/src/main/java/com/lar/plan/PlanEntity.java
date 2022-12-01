package com.lar.plan;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @Column(name = "startTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh")
    private LocalDateTime startTime;
}
