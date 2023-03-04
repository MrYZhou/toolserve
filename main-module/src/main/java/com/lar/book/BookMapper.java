package com.lar.book;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lar.book.model.BookPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
public interface BookMapper extends BaseMapper<BookEntity> {
    List<BookEntity> getList(@Param("page") BookPage page);
}
