package com.lar.book;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.lar.book.model.BookInfo;
import com.lar.book.model.BookPage;
import com.lar.util.CommonUtil;
import com.lar.vo.AppResult;
import com.larry.trans.DictMany;
import com.larry.trans.DictOne;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;
    @Autowired
    private BookMapper bookMapper;

    BookController(BookService bookService
    ) {
        this.bookService = bookService;
    }

    /**
     * 获取列表
     *
     * @param page
     * @return
     */
    @PostMapping("/list")
    @DictMany(value = BookInfo.class, key = "data.records")
    public AppResult<Object> list(@RequestBody @Validated BookPage page) {
//        QueryWrapper<BookEntity> wrapper = new QueryWrapper<>();
//        wrapper.lambda().eq(BookEntity::getName, page.getName());
//        BookPage info = bookService.page(page, wrapper);


        QueryWrapper query = new QueryWrapper();
        query.where("name=" + page.getName());
        Page<BookEntity> paginate = bookMapper.paginate(page.getPageNumber(), page.getPageSize(), query);

        return AppResult.success(paginate);
    }

    /**
     * 查询信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @DictOne(BookInfo.class)
    public AppResult<Object> info(@PathVariable(value = "id") String id) {
        BookEntity info = bookMapper.selectOneById(id);
        return AppResult.success(info);
    }

    /**
     * 保存信息
     *
     * @param info
     * @return
     */
    @PostMapping
    public AppResult<Object> save(@RequestBody @Validated BookInfo info) {
        long l = System.currentTimeMillis();
        BookEntity bookEntity = CommonUtil.toBean(info, BookEntity.class);
        bookMapper.insert(bookEntity);
        System.out.println(System.currentTimeMillis() - l);
        return AppResult.success();
    }

    /**
     * 修改信息
     *
     * @param info
     * @return
     */
    @PutMapping
    public AppResult<Object> update(@RequestBody @Validated BookInfo info) {

        BookEntity bookEntity = CommonUtil.toBean(info, BookEntity.class);
        bookMapper.update(bookEntity);
        return AppResult.success();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public AppResult<Object> delete(@PathVariable String id) {
        bookMapper.deleteById(id);
        return AppResult.success("删除成功");
    }

    @GetMapping("excel")
    public void download(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = "test";
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<BookEntity> list = bookMapper.selectAll();
        EasyExcel.write(response.getOutputStream(), BookEntity.class).sheet("数据").doWrite(list);
    }

    @PostMapping("excel")
    @ResponseBody
    public AppResult<Object> upload(@RequestPart("file") MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), BookEntity.class, new PageReadListener<BookEntity>(
                bookMapper::insertBatch)).sheet().doRead();
        return AppResult.success();
    }

}
