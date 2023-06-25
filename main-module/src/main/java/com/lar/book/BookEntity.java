package com.lar.book;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table("book")
public class BookEntity {
    @TableField("name")
    @ExcelProperty("名字")
    public String name;
    @Id
    @ExcelIgnore
    private String id;


    @TableField("type")
    private Integer type;


    @TableField(value = "tag", fill = FieldFill.INSERT)
    private String tag;
    @TableField(value = "createTime", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "modifyTime", fill = FieldFill.INSERT)
    private LocalDateTime modifyTime;


}
