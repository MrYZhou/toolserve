package com.lar.main.book;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lar.main.book.model.BookPage;

import java.util.List;

public interface BookService extends IService<BookEntity> {
    List<BookEntity> getList(BookPage page);
}
