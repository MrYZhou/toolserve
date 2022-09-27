package com.lar.main.book;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lar.main.book.model.BookPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface BookMapper extends BaseMapper<BookEntity> {
    List<BookEntity> getList(@Param("page") BookPage page);
}
