package com.lar.book.model;

import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
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

    //SIMPLE 翻译，用于关联其他的表进行翻译    schoolName 为 School 的一个字段
    @Trans(type = TransType.SIMPLE,target = Place.class,fields = "name")
    private String placeId;
}
