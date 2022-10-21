package com.lar.main.book;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lar.main.book.model.BookInfo;
import com.lar.main.book.model.BookPage;
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

    @PostMapping("mus")
    public AppResult<Object> mus(@RequestBody @Validated BookPage page) {
        String s = HttpUtil.get("http://v.juhe.cn/laohuangli/d?date=2014-09-11&key=3204cdf5a2a0c36778c880a461b6ea5d");
        return AppResult.success(s);
    }

    @GetMapping
    public AppResult<Object> tuanqi() {
        String data = "{\n" +
                "    \"reason\": \"查询成功\",\n" +
                "    \"result\": {\n" +
                "        \"city\": \"苏州\",\n" +
                "        \"realtime\": {\n" +
                "            \"temperature\": \"4\",\n" +
                "            \"humidity\": \"82\",\n" +
                "            \"info\": \"阴\",\n" +
                "            \"wid\": \"02\",\n" +
                "            \"direct\": \"西北风\",\n" +
                "            \"power\": \"3级\",\n" +
                "            \"aqi\": \"80\"\n" +
                "        },\n" +
                "        \"future\": [\n" +
                "            {\n" +
                "                \"date\": \"2019-02-22\",\n" +
                "                \"temperature\": \"1/7℃\",\n" +
                "                \"weather\": \"小雨转多云\",\n" +
                "                \"wid\": {\n" +
                "                    \"day\": \"07\",\n" +
                "                    \"night\": \"01\"\n" +
                "                },\n" +
                "                \"direct\": \"北风转西北风\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"date\": \"2019-02-23\",\n" +
                "                \"temperature\": \"2/11℃\",\n" +
                "                \"weather\": \"多云转阴\",\n" +
                "                \"wid\": {\n" +
                "                    \"day\": \"01\",\n" +
                "                    \"night\": \"02\"\n" +
                "                },\n" +
                "                \"direct\": \"北风转东北风\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"date\": \"2019-02-24\",\n" +
                "                \"temperature\": \"6/12℃\",\n" +
                "                \"weather\": \"多云\",\n" +
                "                \"wid\": {\n" +
                "                    \"day\": \"01\",\n" +
                "                    \"night\": \"01\"\n" +
                "                },\n" +
                "                \"direct\": \"东北风转北风\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"date\": \"2019-02-25\",\n" +
                "                \"temperature\": \"5/12℃\",\n" +
                "                \"weather\": \"小雨转多云\",\n" +
                "                \"wid\": {\n" +
                "                    \"day\": \"07\",\n" +
                "                    \"night\": \"01\"\n" +
                "                },\n" +
                "                \"direct\": \"东北风\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"date\": \"2019-02-26\",\n" +
                "                \"temperature\": \"5/11℃\",\n" +
                "                \"weather\": \"多云转小雨\",\n" +
                "                \"wid\": {\n" +
                "                    \"day\": \"01\",\n" +
                "                    \"night\": \"07\"\n" +
                "                },\n" +
                "                \"direct\": \"东北风\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"error_code\": 0\n" +
                "}";
        return new AppResult<>();
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
