package com.lar.security.config;

import com.alibaba.fastjson.JSON;
import common.base.AppResult;
import common.enums.AppResultCode;
import common.util.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** 认证失败处理 */
@Component
public class AuthenticationEntryPointConfig implements AuthenticationEntryPoint {
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    AppResult result = AppResult.fail(AppResultCode.AuthorizeFail.getCode(), "认证失败请重新登录");
    String json = JSON.toJSONString(result);
    WebUtils.renderString(response, json);
  }
}
