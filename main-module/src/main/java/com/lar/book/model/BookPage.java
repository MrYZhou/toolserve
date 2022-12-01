package com.lar.book.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lar.book.BookEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
//@Data
//@EqualsAndHashCode(callSuper = false)
//public class BookPage extends Page<BookEntity> {
//    private String name;
//}

@Data
@EqualsAndHashCode(callSuper = false)
public class BookPage extends Page<BookEntity> {
    @NotNull(message = "不能为空")
    private String name;
}