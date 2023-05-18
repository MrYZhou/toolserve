package com.lar.xlj;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lar.book.model.BookPage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class XLJServiceImpl extends ServiceImpl<XLJMapper, XLJEntity> implements XLJService {


    @Override
    public List<XLJEntity> getList(BookPage page) {
        return null;
    }
}
