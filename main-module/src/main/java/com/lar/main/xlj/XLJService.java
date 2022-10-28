package com.lar.main.xlj;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lar.main.book.model.BookPage;

import java.util.List;

public interface XLJService extends IService<XLJEntity> {
    List<XLJEntity> getList(BookPage page);
}
