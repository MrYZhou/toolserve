package com.lar.Student;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class StudentInfo {
    private String id;
    private String name;
    private String age;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")      // 表示接收时间类型
    private Date time;

}