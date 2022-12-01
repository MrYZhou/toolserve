package com.lar.security.config;

import com.alibaba.fastjson.JSON;
import common.base.AppResult;
import common.enums.AppResultCode;
import common.util.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** 授权失败处理 */
@Component
public class AccessDeniedHandlerConfig implements AccessDeniedHandler {
  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException, ServletException {
    AppResult result = AppResult.fail(AppResultCode.AuthorizeFail.getCode(), "权限不足");
    String json = JSON.toJSONString(result);

    WebUtils.renderString(response, json);
  }
}
