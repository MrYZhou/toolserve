package com.lar.main.book;


import com.lar.common.aop.MenuCheck;
import com.lar.common.vo.AppResult;
import com.lar.main.book.model.BookModel;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * 控制层
 */
@RestController
@RequestMapping("/book")
public class BookController {
    @PostMapping("list")
    public AppResult<?> list(){
        BookModel bookModel = new BookModel();
        bookModel.setDate(new Date());
        bookModel.setLocalDateTime(LocalDateTime.now());
        bookModel.setLocalDate(LocalDate.now());
        bookModel.setLocalTime(LocalTime.now());
        // bookModel.setName("Java");
        return AppResult.success(bookModel);
    }
    @GetMapping("/{id}")
    public AppResult<?> getInfo(@PathVariable String id){
        BookModel bookModel = new BookModel();
        bookModel.setName("111");
        bookModel.setPrice(10.0);
        return AppResult.success(bookModel);
    }
    @PostMapping
    @MenuCheck
    public AppResult<?> save(@RequestBody BookModel bookModel){

        return AppResult.success(bookModel);
    }
}
