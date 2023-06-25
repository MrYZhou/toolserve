package com.lar.book;

import com.lar.book.model.BookPage;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface BookMapper extends BaseMapper<BookEntity> {
    List<BookEntity> getList(@Param("page") BookPage page);
}
