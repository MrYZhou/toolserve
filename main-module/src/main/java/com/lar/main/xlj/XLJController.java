package com.lar.main.xlj;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lar.main.xlj.model.HuResult;
import com.lar.main.xlj.model.HuRootBean;
import common.base.AppResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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
    public AppResult<Object> yangli(String date) {

        String s = HttpUtil.get("http://v.juhe.cn/laohuangli/d?date=" + date);
        HuRootBean bean = JSONUtil.toBean(s, HuRootBean.class);
        return AppResult.success(bean.getResult());
    }
    // 获取天气
    @GetMapping("tianqi")
    public AppResult<Object> tianqi(String city) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("city", city);
        map.put("key","f918860905f9963bed25b26778189c97");
        String s = HttpUtil.get("http://apis.juhe.cn/simpleWeather/query", map);
        // 天气
        HuRootBean bean = JSONUtil.toBean(s, HuRootBean.class);

        // 生活指数
        return AppResult.success();
    }
    @GetMapping("life")
    public AppResult<Object> life(String city) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("city", city);
        map.put("key","f918860905f9963bed25b26778189c97");
        String s = HttpUtil.get("http://apis.juhe.cn/simpleWeather/life", map);

        HuRootBean bean = JSONUtil.toBean(s, HuRootBean.class);
        HuResult result = bean.getResult();
        // 生活指数
        return AppResult.success(result.getLife());
    }




    /**
     * 笑话废弃,都没有更新新笑话
     * @param data
     * @return
     */
    @GetMapping("joker")
    public AppResult<Object> joker(@RequestBody QueryData data) {
        String s = HttpUtil.get("http://v.juhe.cn/joke/content/list.php");
        HashMap<String, Object> map = new HashMap<>();
        map.put("sort", "desc");
        map.put("time","");

//        最大20
//        map.put("page",data.getPage());
//        map.put("pageSize",data.getPagesize());
        map.put("key", xiaohuaKey);
        HuRootBean bean = JSONUtil.toBean(s, HuRootBean.class);
        return AppResult.success();
    }



}
