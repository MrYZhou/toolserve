package com.lar.main.example;

import lombok.Data;

@Data
public class TableInfo {

    String dbName;
    String tableComment;
    String columnName;

    String columnComment;
    //    字段数据类型
    String dataType;

}
