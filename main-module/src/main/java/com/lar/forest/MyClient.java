package com.lar.forest;

import com.dtflys.forest.annotation.Get;
import org.springframework.stereotype.Component;


public interface MyClient {

    @Get("http://localhost:8080/hello")
    String helloForest();

}
