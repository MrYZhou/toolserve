package com.lar.book;

import com.lar.vo.AppResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/book")
public class BookController {
    @GetMapping
    public AppResult<?> list(){
        BookModel bookModel = new BookModel();
        bookModel.setDate(new Date());
        bookModel.setLocalDateTime(LocalDateTime.now());
        return AppResult.success(bookModel);
    }
}
