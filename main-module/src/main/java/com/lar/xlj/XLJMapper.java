package com.lar.xlj;

import com.lar.book.model.BookPage;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface XLJMapper extends BaseMapper<XLJEntity> {
    List<XLJEntity> getList(@Param("page") BookPage page);
}
