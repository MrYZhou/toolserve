package com.lar.forest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/forest")
public class ForestController {
    @Autowired
    MyService myService;
    public void getdata(){
        myService.testClient();
    }
}
