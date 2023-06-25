package com.lar.book;

import com.lar.book.model.BookPage;

import java.util.List;

public interface BookService {
    List<BookEntity> getList(BookPage page);
}
