package com.lar.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.annotation.SaCheckBasic;
import cn.dev33.satoken.annotation.SaCheckDisable;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaCheckSafe;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * [Sa-Token 权限认证] 配置类
 *
 * @author click33
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    /**
     * 注册 [Sa-Token 全局过滤器]
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()

                // 指定 [拦截路由] 与 [放行路由]
                .addInclude("/**").addExclude("/favicon.ico")

                // 认证函数: 每次请求执行
                .setAuth(obj -> {
                    SaManager.getLog().debug("----- 请求path={}  提交token={}", SaHolder.getRequest().getRequestPath(), StpUtil.getTokenValue());
                    // ...
                })

                // 异常处理函数：每次认证函数发生异常时执行此函数
                .setError(e -> {
                    return SaResult.error(e.getMessage());
                })

                // 前置函数：在每次认证函数之前执行
                .setBeforeAuth(obj -> {
                    SaHolder.getResponse()

                            // ---------- 设置跨域响应头 ----------
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", "*")
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", "*")
                            // 允许的header参数
                            .setHeader("Access-Control-Allow-Headers", "*")
                            // 有效时间
                            .setHeader("Access-Control-Max-Age", "3600")
                    ;

                    // 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            .free(r -> System.out.println("--------OPTIONS预检请求，不做处理"))
                            .back();
                })
                ;
    }

    /**
     * 注意：拦截器模式，只能在Controller层进行注解鉴权，当需要在service层也可以使用需要使用 AOP注解鉴权
     *
     * Sa-Token 整合 SpringAOP 实现注解鉴权
     * <dependency>
     *     <groupId>cn.dev33</groupId>
     *     <artifactId>sa-token-spring-aop</artifactId>
     *     <version>1.34.0</version>
     * </dependency>
     *
     * 但如果引入需要删除此方法，否则会在Controller层校验两次
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /** 注册 Sa-Token 拦截器，打开注解式鉴权功能
         *         @SaCheckLogin: 登录校验 —— 只有登录之后才能进入该方法。
         *         @SaCheckRole("admin"): 角色校验 —— 必须具有指定角色标识才能进入该方法。
         *         @SaCheckPermission("user:add"): 权限校验 —— 必须具有指定权限才能进入该方法。
         *         @SaCheckSafe: 二级认证校验 —— 必须二级认证之后才能进入该方法。
         *         @SaCheckBasic: HttpBasic校验 —— 只有通过 Basic 认证后才能进入该方法。
         *         @SaCheckDisable("comment")：账号服务封禁校验 —— 校验当前账号指定服务是否被封禁。
         *         @SaIgnore：忽略校验 —— 表示被修饰的方法或类无需进行注解鉴权和路由拦截器鉴权,可以游客访问。
        */
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }

}
