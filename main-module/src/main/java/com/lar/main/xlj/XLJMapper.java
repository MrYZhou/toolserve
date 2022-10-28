package com.lar.main.xlj;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lar.main.book.model.BookPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface XLJMapper extends BaseMapper<XLJEntity> {
    List<XLJEntity> getList(@Param("page") BookPage page);
}
