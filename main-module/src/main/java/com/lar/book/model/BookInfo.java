package com.lar.book.model;

import com.larry.trans.DictValue;
import lombok.Data;

@Data
public class BookInfo {

    String id;
    String name;

    String tag;
    @DictValue(ref = "book")
    String type;

}
