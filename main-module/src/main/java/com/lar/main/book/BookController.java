package com.lar.main.book;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lar.main.book.model.BookInfo;
import com.lar.main.book.model.BookPage;
import common.base.AppResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @PostMapping("list")
    public AppResult<Object> list(@RequestBody @Validated BookPage page) {
        QueryWrapper<BookEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BookEntity::getName, page.getName());
        BookPage info = bookService.page(page, wrapper);
        return AppResult.success(info);
    }

    @GetMapping("/{id}")
    public AppResult<Object> info(@PathVariable String id) {

        BookEntity info = bookService.getById(id);
        return AppResult.success(info);
    }

    @PostMapping
    public AppResult<Object> save(@RequestBody @Validated BookInfo info) {
        BookEntity bookEntity = BeanUtil.copyProperties(info, BookEntity.class);
        bookService.save(bookEntity);
        return AppResult.success();
    }

    @PutMapping
    public AppResult<Object> update(@RequestBody @Validated BookInfo info) {

        BookEntity bookEntity = BeanUtil.copyProperties(info, BookEntity.class);
        bookService.updateById(bookEntity);
        return AppResult.success();
    }

    @DeleteMapping("/{id}")
    public AppResult<Object> delete(@PathVariable String id) {
        boolean b = bookService.removeById(id);
        return AppResult.success(b);
    }

    BookController(BookService bookService
    ) {
        this.bookService = bookService;
    }

}
