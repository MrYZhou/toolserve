package com.lar.main.book;


import com.lar.common.vo.AppResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@RestController
@RequestMapping("/book")
public class BookController {
    @GetMapping
    public AppResult<?> list(){
        com.lar.book.BookModel bookModel = new com.lar.book.BookModel();
        bookModel.setDate(new Date());
        bookModel.setLocalDateTime(LocalDateTime.now());
        bookModel.setLocalDate(LocalDate.now());
        bookModel.setLocalTime(LocalTime.now());
        return AppResult.success(bookModel);
    }
    @PostMapping
    public AppResult<?> save(@RequestBody com.lar.book.BookModel bookModel){

        return AppResult.success(bookModel);
    }
}
