package common.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppResult<T> {

    private Integer code;

    private String msg;

    private T data;

    public AppResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // public AppResult() {}

    public static AppResult<Object> success() {
        AppResult<Object> jsonData = new AppResult<Object>();
        jsonData.setCode(200);
        jsonData.setMsg("Success");
        return jsonData;
    }

    public static AppResult<Object> success(String msg) {
        AppResult<Object> jsonData = new AppResult<Object>();
        jsonData.setCode(200);
        jsonData.setMsg(msg);
        return jsonData;
    }

    public static AppResult<Object> success(Object object) {
        AppResult<Object> jsonData = new AppResult<Object>();
        jsonData.setData(object);
        jsonData.setCode(200);
        jsonData.setMsg("Success");
        return jsonData;
    }

    public static <T> AppResult<Object> page(List<T> list, PaginationVO<T> pagination) {
        AppResult<Object> jsonData = new AppResult<Object>();
        PageListVO<T> vo = new PageListVO<>();
        vo.setList(list);
        vo.setPagination(pagination);
        jsonData.setData(vo);
        jsonData.setCode(200);
        jsonData.setMsg("Success");
        return jsonData;
    }

    public static AppResult<Object> success(String msg, Object object) {
        AppResult<Object> jsonData = new AppResult<Object>();
        jsonData.setData(object);
        jsonData.setCode(200);
        jsonData.setMsg(msg);
        return jsonData;
    }

    public static AppResult<Object> fail(Integer code, String message) {
        AppResult<Object> jsonData = new AppResult<Object>();
        jsonData.setCode(code);
        jsonData.setMsg(message);
        return jsonData;
    }

    public static AppResult<Object> fail(String msg) {
        AppResult<Object> jsonData = new AppResult<Object>();
        jsonData.setMsg(msg);
        jsonData.setCode(400);
        return jsonData;
    }
}
