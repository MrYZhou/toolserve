package com.lar.Student;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;


@Data
@TableName("la_student")
public class StudentEntity {
    @TableId("f_id")
    private String  id;
    /**
    名字
     */
    @TableField("f_name")
    private String name;
    /**
    年龄
     */
    @TableField("f_age")
    private String age;
    /**
    时间
     */
    @TableField("time")
    private LocalDateTime time;
    
}