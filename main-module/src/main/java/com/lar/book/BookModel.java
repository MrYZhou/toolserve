package com.lar.book;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class BookModel {
    private Date date;
    private LocalDateTime localDateTime;
}
