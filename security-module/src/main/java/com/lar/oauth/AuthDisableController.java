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

    @PostMapping("/disabledLevel")
    public AppResult<?> disabledLevel(@RequestBody UserView user) throws SQLException {

        // 封禁指定用户评论能力，期限为 1天
        // time=-1代表永久封禁
        StpUtil.disable(10001, "comment", 86400);
        // 判断：指定账号的指定服务 是否已被封禁 (true=已被封禁, false=未被封禁)
        StpUtil.isDisable(10001, "comment");

        // 校验：指定账号的指定服务 是否已被封禁，如果被封禁则抛出异常 `DisableServiceException`
        StpUtil.checkDisable(10001, "comment");

        return AppResult.success();
    }

    // 封禁指定账号  ---- http://localhost:8081/disable/disable?userId=10001
    @RequestMapping("disable")
    public AppResult<?> disable(long userId) {
        /*
         * 账号封禁：
         * 	参数1：要封禁的账号id
         * 	参数2：要封禁的时间，单位：秒，86400秒=1天
         */
        StpUtil.disable(userId, 86400);
        return AppResult.success("账号 " + userId + " 封禁成功");
    }

    // 解封指定账号  ---- http://localhost:8081/disable/untieDisable?userId=10001
    @RequestMapping("untieDisable")
    public AppResult<?> untieDisable(long userId) {
        StpUtil.untieDisable(userId);
        return AppResult.success("账号 " + userId + " 解封成功");
    }


}
