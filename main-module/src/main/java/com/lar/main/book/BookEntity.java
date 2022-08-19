package com.lar.main.book;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("book")
public class BookEntity {
    @TableId(value = "id")
    @ExcelIgnore
    private String id;

    @TableField("name")
    @ExcelProperty("名字")
    private String name;
}
