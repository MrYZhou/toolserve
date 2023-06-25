package com.lar.book.model;

import com.lar.book.BookEntity;
import com.mybatisflex.core.paginate.Page;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class BookPage extends Page<BookEntity> {
    @NotNull(message = "不能为空")
    private String name;
}
