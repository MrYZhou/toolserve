package common.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppResult<T> {

    private Integer code;

    private String msg;

    private T data;

    public static AppResult success() {
        AppResult jsonData = new AppResult();
        jsonData.setCode(200);
        jsonData.setMsg("Success");
        return jsonData;
    }

    public static AppResult success(String msg) {
        AppResult jsonData = new AppResult();
        jsonData.setCode(200);
        jsonData.setMsg(msg);
        return jsonData;
    }

    public static AppResult success(Object object) {
        AppResult jsonData = new AppResult();
        jsonData.setData(object);
        jsonData.setCode(200);
        jsonData.setMsg("Success");
        return jsonData;
    }

    public static<T> AppResult page(List<T> list, PaginationVO pagination) {
        AppResult jsonData = new AppResult();
        PageListVO<T> vo = new PageListVO<>();
        vo.setList(list);
        vo.setPagination(pagination);
        jsonData.setData(vo);
        jsonData.setCode(200);
        jsonData.setMsg("Success");
        return jsonData;
    }



    public static AppResult success(String msg, Object object) {
        AppResult jsonData = new AppResult();
        jsonData.setData(object);
        jsonData.setCode(200);
        jsonData.setMsg(msg);
        return jsonData;
    }

    public static AppResult fail(Integer code, String message) {
        AppResult jsonData = new AppResult();
        jsonData.setCode(code);
        jsonData.setMsg(message);
        return jsonData;
    }


    public static AppResult fail(String msg) {
        AppResult jsonData = new AppResult();
        jsonData.setMsg(msg);
        jsonData.setCode(400);
        return jsonData;
    }
}
