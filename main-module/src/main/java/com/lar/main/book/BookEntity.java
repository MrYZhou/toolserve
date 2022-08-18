package com.lar.main.book;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("book")
public class BookEntity {
    @TableId(value = "id")
    private String id;
    @TableField("name")
    private String name;
}
