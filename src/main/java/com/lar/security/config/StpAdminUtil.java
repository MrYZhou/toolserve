package com.lar.security.config;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.stp.StpLogic;

public class StpAdminUtil {

    /**
     * 账号体系标识,用于在一套系统实现多套账号体系
     */
    public static final String TYPE = "admin";

    // 使用匿名子类 重写`stpLogic对象`的一些方法
    public static StpLogic stpLogic = new StpLogic("admin") {

        // 首先自定义一个 Config 对象
        final SaTokenConfig config = new SaTokenConfig()
                .setTokenName("satoken")
                .setTimeout(2592000)
                // ... 其它set
                ;

        // 然后重写 stpLogic 配置获取方法
        @Override
        public SaTokenConfig getConfig() {
            return config;
        }

        // 重写 StpLogic 类下的 `splicingKeyTokenName` 函数，返回一个与 `StpUtil` 不同的token名称, 防止冲突
        @Override
        public String splicingKeyTokenName() {
            return super.splicingKeyTokenName() + "-admin";
        }
    };

    public static void checkPermission(String admin) {

    }
}
