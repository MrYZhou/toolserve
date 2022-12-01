package com.lar.book;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lar.book.model.BookInfo;
import com.lar.book.model.BookPage;
import common.base.AppResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @PostMapping("list")
    public AppResult<Object> list(@RequestBody @Validated BookPage page) {
        QueryWrapper<BookEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BookEntity::getName, page.getName());
        BookPage info = bookService.page(page, wrapper);
//        List<BookEntity> books = bookService.getList(page);
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

    @GetMapping("excel")
    public void download(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = "test";
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<BookEntity> list = bookService.list();

        EasyExcel.write(response.getOutputStream(), BookEntity.class).sheet("数据").doWrite(list);
    }

    @PostMapping("excel")
    @ResponseBody
    public AppResult<Object> upload(@RequestPart("file") MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), BookEntity.class, new PageReadListener<BookEntity>(
                bookService::saveBatch)).sheet().doRead();
        return AppResult.success();
    }

    BookController(BookService bookService
    ) {
        this.bookService = bookService;
    }

}
