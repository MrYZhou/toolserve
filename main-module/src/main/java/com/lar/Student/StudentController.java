package com.lar.Student;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lar.common.base.AppResult;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class  StudentController {
    @Autowired
    private StudentService studentService;
    /**
     * 获取列表
     * @param page
     * @return
     */
    @PostMapping("list")
    public AppResult<T> list(@RequestBody @Validated StudentQuery page) {
        QueryWrapper<StudentEntity> wrapper = new QueryWrapper<>();

        
        
        StudentQuery info = studentService.page(page, wrapper);
        
        return AppResult.success(info);
    }
    /**
     * 查询信息 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public AppResult<T> info(@PathVariable String id) {

        StudentEntity info = studentService.getById(id);
        return AppResult.success(info);
    }
    /**
     * 保存信息
     * @param info
     * @return
     */
    @PostMapping("save")
    public AppResult<T> save(@RequestBody @Validated StudentInfo info) {
        StudentEntity studentEntity = BeanUtil.copyProperties(info, StudentEntity.class);

        studentService.save(studentEntity);
        return AppResult.success("保存成功");
    }
    /**
     * 更新信息
     * @param info
     * @return
     */
    @PutMapping("update")
    public AppResult<T> update(@RequestBody @Validated StudentInfo info) {

        StudentEntity studentEntity = BeanUtil.copyProperties(info, StudentEntity.class);
        studentService.updateById(studentEntity);
        return AppResult.success("更新成功");
    }
    /**
     * 删除信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public AppResult<T> delete(@PathVariable String id) {
        studentService.removeById(id);
        return AppResult.success("删除成功");
    }

    // @GetMapping("excel")
    // public void download(HttpServletResponse response) throws IOException {
    //     response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    //     response.setCharacterEncoding("utf-8");
    //     String fileName = "test";
    //     response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

    //     List<StudentEntity> list = studentService.list();

    //     EasyExcel.write(response.getOutputStream(), StudentEntity.class).sheet("数据").doWrite(list);
    // }

    // @PostMapping("excel")
    // @ResponseBody
    // public AppResult<T> upload(@RequestPart("file") MultipartFile file) throws IOException {
    //     EasyExcel.read(file.getInputStream(), StudentEntity.class, new PageReadListener<StudentEntity>(
    //             studentService::saveBatch)).sheet().doRead();
    //     return AppResult.success();
    // }

  

}