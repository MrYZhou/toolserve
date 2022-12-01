package com.lar.xlj;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lar.book.model.BookPage;

import java.util.List;

public interface XLJService extends IService<XLJEntity> {
    List<XLJEntity> getList(BookPage page);
}
