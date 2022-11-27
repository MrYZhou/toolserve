package com.lar.main.xlj;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lar.main.xlj.model.HuResult;
import com.lar.main.xlj.model.HuRootBean;
import com.lar.main.xlj.model.JsonRootBeanDream;
import com.lar.main.xlj.model.ResultDream;
import common.base.AppResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/xlj")
public class XLJController {
    @Value("${api.yangli}")
    String yangliKey = "";

    @Value("${api.xiaohua}")
    String xiaohuaKey = "";

    /**
     * 获取黄历
     * @param date
     * @return
     */
    @PostMapping("yangli")
    public AppResult<Object> yangli(String date) {

        String s = HttpUtil.get("http://v.juhe.cn/laohuangli/d?date=" + date);
        HuRootBean bean = JSONUtil.toBean(s, HuRootBean.class);
        return AppResult.success(bean.getResult());
    }

    /**
     * 周工解梦
     * @param q
     * @return
     */
    @GetMapping("dream")
    public AppResult<Object> dream(String q) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("q", q);
        map.put("key", "b1fb11350da7fac78428558e2739f367");
        map.put("full", 1);
        String s = HttpUtil.get("http://v.juhe.cn/dream/query", map);

        JsonRootBeanDream bean = JSONUtil.toBean(s, JsonRootBeanDream.class);
        List<ResultDream> result = bean.getResult();
        return AppResult.success(result);
    }

    /**
     * 随机头像
     * @param q 头像类型
     * @return 头像地址
     */
    @GetMapping("toux")
    public AppResult<Object> toux(String q) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("sort", q);
        map.put("format", "json");
        String s="";
        s  = HttpUtil.get("https://api.uomg.com/api/rand.avatar", map);

        Map map1 = JSONUtil.toBean(s, Map.class);

        return AppResult.success(map1);
    }

    /**
     * 获取天气
     * @param city
     * @return
     */
    @GetMapping("tianqi")
    public AppResult<Object> tianqi(String city) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("city", city);
        map.put("key", "f918860905f9963bed25b26778189c97");
        String s = HttpUtil.get("http://apis.juhe.cn/simpleWeather/query", map);
        // 天气
        Map map1 = JSONUtil.toBean(s, Map.class);
        return AppResult.success(map1);
    }

    /**
     * 生活指数
     * @param city
     * @return
     */
    @GetMapping("life")
    public AppResult<Object> life(String city) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("city", city);
        map.put("key", "f918860905f9963bed25b26778189c97");
        String s = HttpUtil.get("http://apis.juhe.cn/simpleWeather/life", map);

        HuRootBean bean = JSONUtil.toBean(s, HuRootBean.class);
        HuResult result = bean.getResult();
        return AppResult.success(result.getLife());
    }



    /**
     * 获取空气质量
     * @param location
     * @return
     */
    @GetMapping("kongqi")
    public AppResult<Object> kongqi(String location) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("location", location);
        map.put("key", "195e71c6381f43bba07618ac35dc3ca5");
        map.put("lang", "zh");
        String s = HttpUtil.get("https://devapi.qweather.com/v7/air/5d", map);
        Map res = JSONUtil.toBean(s, Map.class);
        return AppResult.success(res);
    }



    /**
     * 笑话废弃,都没有更新新笑话
     *
     * @param data
     * @return
     */
    @GetMapping("joker")
    public AppResult<Object> joker(@RequestBody QueryData data) {
        String s = HttpUtil.get("http://v.juhe.cn/joke/content/list.php");
        HashMap<String, Object> map = new HashMap<>();
        map.put("sort", "desc");
        map.put("time", "");

//        最大20
//        map.put("page",data.getPage());
//        map.put("pageSize",data.getPagesize());
        map.put("key", xiaohuaKey);
        HuRootBean bean = JSONUtil.toBean(s, HuRootBean.class);
        return AppResult.success(bean);
    }


}
