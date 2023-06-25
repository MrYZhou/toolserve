package com.lar.xlj;

import com.lar.book.model.BookPage;

import java.util.List;

public interface XLJService {
    List<XLJEntity> getList(BookPage page);
}
