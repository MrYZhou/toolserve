package com.lar.book.model;

import com.fhs.core.trans.vo.TransPojo;
import lombok.Data;

@Data
public class BookInfo implements TransPojo {

    String id;
    String name;

    String tag;
    // 字典翻译 ref为非必填
//    @Trans(type = TransType.DICTIONARY, key = "book")
    @DictValue(ref = "book")
    String type;
}
