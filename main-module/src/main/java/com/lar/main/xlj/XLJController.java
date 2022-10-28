package com.lar.main.xlj;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lar.main.xlj.model.HuRootBean;
import common.base.AppResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/xlj")
public class XLJController {
    @Value("${api.yangli}")
    String yangliKey = "";

    @Value("${api.xiaohua}")
    String xiaohuaKey = "";

    //获取黄历
    @PostMapping("yangli")
    public AppResult<Object> yangli() {

        String s = HttpUtil.get("http://v.juhe.cn/laohuangli/d?date=2014-09-11&key=" + yangliKey);
        HuRootBean bean = JSONUtil.toBean(s, HuRootBean.class);
        return AppResult.success(bean.getResult());
    }

    @GetMapping("joker")
    public AppResult<Object> joker() {
        String s = HttpUtil.get("http://v.juhe.cn/laohuangli/d?date=2014-09-11&key=3204cdf5a2a0c36778c880a461b6ea5d");
        HashMap<String, Object> map = new HashMap<>();
//        map.put("city", city);
        map.put("key", xiaohuaKey);
        HuRootBean bean = JSONUtil.toBean(s, HuRootBean.class);
        return AppResult.success();
    }

    // 获取天气
    @GetMapping
    public AppResult<Object> tianqi(String city) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("city", city);
        map.put("key", xiaohuaKey);
        String s = HttpUtil.get("http://apis.juhe.cn/simpleWeather/query", map);
        HuRootBean bean = JSONUtil.toBean(s, HuRootBean.class);
        return AppResult.success();
    }

}
