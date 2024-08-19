package com.lar.common.config.exception;

import com.alibaba.fastjson.JSON;
import com.lar.common.enums.AppResultCode;
import com.lar.common.vo.AppResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ResponseBody
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public AppResult<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
    String message = e.getMessage();
    String json = JSON.toJSONString(message);
    return AppResult.fail(AppResultCode.ValidateError.getCode(), json);
  }

  @ResponseBody
  @ExceptionHandler(RuntimeException.class)
  public AppResult<?> runTimeException(RuntimeException e) {
    String message = e.getMessage();
    String json = JSON.toJSONString(message);
    return AppResult.fail(AppResultCode.ValidateError.getCode(), json);
  }
  // 全局异常拦截
  @ExceptionHandler
  public AppResult<?> handlerException(Exception e) {
    return AppResult.fail(e.getMessage());
  }
}
