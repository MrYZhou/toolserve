package com.lar.main.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataSourceController {

    @GetMapping("connect")
    public void connect(@RequestBody DbInfo dbinfo) {

//        DataSource ds = new SimpleDataSource("jdbc:mysql://" + uri + ":" + port + "/" + dbName, user, password);
        DbUtil.gettableinfo();
    }
}
