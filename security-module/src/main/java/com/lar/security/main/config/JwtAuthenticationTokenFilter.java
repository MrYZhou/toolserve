package com.lar.security.main.config;

import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.lar.security.main.model.LoginUser;
import common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/** jwt过滤器 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

  @Autowired private RedisUtil redisUtil;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // 获取token
    String token = request.getHeader("token");
    if (!StringUtils.hasText(token)) {
      // 放行
      filterChain.doFilter(request, response);
      return;
    }
    // 解析token
    String userid;
    try {
      JWT jwt = JWTUtil.parseToken(token);
      userid = String.valueOf(jwt.getPayload("uid"));

      //      userid = claims.getSubject();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("token非法");
    }
    // 从redis中获取用户信息
    String redisKey = "login" + userid;
    String s = redisUtil.get(redisKey);
    LoginUser loginUser = JSONUtil.toBean(s, LoginUser.class);
    if (Objects.isNull(loginUser.getUser())) {
      throw new RuntimeException("用户未登录");
    }
    // 存入SecurityContextHolder,后面过滤器都会从这边拿认证信息
    // TODO 获取权限信息封装到Authentication中

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    // 放行
    filterChain.doFilter(request, response);
  }
}
