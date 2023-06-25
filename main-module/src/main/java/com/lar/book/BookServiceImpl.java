package com.lar.book;

import com.lar.book.model.BookPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookMapper bookMapper;

    @Override
    public List<BookEntity> getList(BookPage page) {
        return bookMapper.getList(page);
    }
}
