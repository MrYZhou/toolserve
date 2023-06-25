package com.lar.book;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table("book")
public class BookEntity {
    @Id
    @ExcelIgnore
    private String id;

    @Column("name")
    @ExcelProperty("名字")
    public String name;

    @Column("type")
    private Integer type;


    @Column(value = "tag")
    private String tag;
    @Column(value = "createTime")
    private LocalDateTime createTime;
    @Column(value = "modifyTime")
    private LocalDateTime modifyTime;


}
