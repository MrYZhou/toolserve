package com.lar.xlj;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table("xlj")
public class XLJEntity {
    @TableId(value = "id")
    @ExcelIgnore
    private String id;

    @TableField("name")
    @ExcelProperty("名字")
    private String name;


    @TableField(value = "tag", fill = FieldFill.INSERT)
    private String tag;
    @TableField(value = "createTime", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "modifyTime", fill = FieldFill.INSERT)
    private LocalDateTime modifyTime;
}
