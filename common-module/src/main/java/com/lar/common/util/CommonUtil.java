package com.lar.common.util;

import org.noear.snack.ONode;

import java.util.List;

public class CommonUtil {
    public static<T> T toBean(Object obj,Class<?> tClass){
        return   ONode.deserialize(ONode.stringify(obj), tClass);
    }

    public static<T> T toBean(String str,Class<?> tClass){
        return   ONode.deserialize(str, tClass);
    }

}
