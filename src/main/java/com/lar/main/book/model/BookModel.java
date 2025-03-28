package com.lar.main.book.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * 前后端转换模型
 */
@Data
public class BookModel {
    @JsonFormat(pattern="yyyy-MM",locale="zh")
    private Date date;
    private LocalDateTime localDateTime;
    private LocalDate localDate;
    private LocalTime localTime;
    private String name;
    private Double price;
}
