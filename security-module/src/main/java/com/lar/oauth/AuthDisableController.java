package com.lar.oauth;

import cn.dev33.satoken.stp.StpUtil;
import com.lar.user.model.UserView;
import com.lar.vo.AppResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/user")
@Slf4j
public class AuthDisableController {
    @PostMapping("/disabledAccount")
    public AppResult<?> disabledAccount(@RequestBody UserView user) throws SQLException {
// 封禁指定账号
        StpUtil.disable(10001, 86400);

// 获取指定账号是否已被封禁 (true=已被封禁, false=未被封禁)
        StpUtil.isDisable(10001);

// 校验指定账号是否已被封禁，如果被封禁则抛出异常 `DisableServiceException`
        StpUtil.checkDisable(10001);

// 获取指定账号剩余封禁时间，单位：秒，如果该账号未被封禁，则返回-2
        StpUtil.getDisableTime(10001);

// 解除封禁
        StpUtil.untieDisable(10001);

        return AppResult.success();
    }

    @PostMapping("/disabledServer")
    public AppResult<?> disabledClassify(@RequestBody UserView user) throws SQLException {

        // 封禁指定用户评论能力，期限为 1天
        // time=-1代表永久封禁
        StpUtil.disable(10001, "comment", 86400);
        // 判断：指定账号的指定服务 是否已被封禁 (true=已被封禁, false=未被封禁)
        StpUtil.isDisable(10001, "comment");

        // 校验：指定账号的指定服务 是否已被封禁，如果被封禁则抛出异常 `DisableServiceException`
        StpUtil.checkDisable(10001, "comment");

        return AppResult.success();
    }
}
