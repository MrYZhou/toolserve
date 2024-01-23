package com.lar.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppResult<T>{

    private Integer code;

    private String msg;

    private T data;


    public static <T> AppResult<T> success() {
        AppResult<T> jsonData = new AppResult<T>();
        jsonData.setCode(200);
        jsonData.setMsg("Success");
        return jsonData;
    }

    public static <T> AppResult<T> success(String msg) {
        AppResult<T> jsonData = new AppResult<T>();
        jsonData.setCode(200);
        jsonData.setMsg(msg);
        return jsonData;
    }

    public static <T> AppResult<T> success(Object object) {
        AppResult<T> jsonData = new AppResult<T>();
        jsonData.setData((T) object);
        jsonData.setCode(200);
        jsonData.setMsg("Success");
        return jsonData;
    }

    public static <T> AppResult<T> page(List<T> list, PaginationVO<T> pagination) {
        AppResult<T> jsonData = new AppResult<T>();
        PageListVO<T> vo = new PageListVO<>();
        vo.setList(list);
        vo.setPagination(pagination);
        jsonData.setData ((T) vo);
        jsonData.setCode(200);
        jsonData.setMsg("Success");
        return jsonData;
    }

    public static <T> AppResult<T> success(String msg, T object) {
        AppResult<T> jsonData = new AppResult<T>();
        jsonData.setData( object);
        jsonData.setCode(200);
        jsonData.setMsg(msg);
        return jsonData;
    }

    public static <T> AppResult<T> fail(Integer code, String message) {
        AppResult<T> jsonData = new AppResult<T>();
        jsonData.setCode(code);
        jsonData.setMsg(message);
        return jsonData;
    }

    public static <T> AppResult<T> fail(String msg) {
        AppResult<T> jsonData = new AppResult<T>();
        jsonData.setMsg(msg);
        jsonData.setCode(400);
        return jsonData;
    }
}
