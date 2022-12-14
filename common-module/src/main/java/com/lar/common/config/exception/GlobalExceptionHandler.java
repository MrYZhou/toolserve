package com.lar.common.config.exception;

import com.alibaba.fastjson.JSON;
import com.lar.common.base.AppResult;
import com.lar.common.enums.AppResultCode;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestController
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ResponseBody
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public AppResult methodArgumentNotValidException(MethodArgumentNotValidException e) {
    Map<String, String> map = new HashMap<>(16);
    String message = e.getMessage();
    String json = JSON.toJSONString(message);
    AppResult result = AppResult.fail(AppResultCode.ValidateError.getCode(), json);
    return result;
  }

  @ResponseBody
  @ExceptionHandler(RuntimeException.class)
  public AppResult runTimeException(RuntimeException e) {
    Map<String, String> map = new HashMap<>(16);
    String message = e.getMessage();
    String json = JSON.toJSONString(message);
    AppResult result = AppResult.fail(AppResultCode.ValidateError.getCode(), json);
    return result;
  }
}
