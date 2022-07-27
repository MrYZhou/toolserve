package com.lar.main.example;

import cn.hutool.db.ds.simple.SimpleDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DbUtil {
    static void gettableinfo() {
        DataSource ds = new SimpleDataSource("jdbc:mysql://localhost:3307/study", "root", "123456");
        String sql = "SELECT\n" +
                "    TB.TABLE_NAME as dbName,      -- 表名\n" +
                "    TB.TABLE_COMMENT as tableComment,   -- 表名注释\n" +
                "    COL.COLUMN_NAME as columnName,    -- 字段名\n" +
                "    COL.COLUMN_COMMENT as columnComment,  -- 字段注释\n" +
                "\t\tCOL.DATA_TYPE   as dataType     -- 字段数据类型\n" +
                "FROM\n" +
                "    INFORMATION_SCHEMA.TABLES TB,\n" +
                "    INFORMATION_SCHEMA.COLUMNS COL\n" +
                "Where TB.TABLE_SCHEMA ='" + "study" + "' AND TB.TABLE_NAME = COL.TABLE_NAME";
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = ds.getConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            List<TableInfo> list = new ArrayList<>();
            while (resultSet.next()) {
                TableInfo tableInfo = new TableInfo();
                tableInfo.setDbName(resultSet.getString("dbName"));
                tableInfo.setTableComment(resultSet.getString("tableComment"));
                tableInfo.setColumnName(resultSet.getString("columnName"));
                tableInfo.setColumnComment(resultSet.getString("columnComment"));
                tableInfo.setDataType(resultSet.getString("dataType"));
                list.add(tableInfo);
            }
            // 如果要算总数，可拼接后查询得到
//            select count(*) as num,'aa'  as tableName from plan UNION all
//            select count(*) as num,'aa1' as tableName from sys_menu
            
        } catch (SQLException e) {
            log.info("错误", e);
        } finally {
            cn.hutool.db.DbUtil.close(conn, statement, resultSet);
        }
    }
}
