package com.lar.main.xlj.model;


import lombok.Data;

import java.util.Date;

/**
 * Auto-generated: 2022-10-21 17:14:30
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 * 黄历结果
 */
@Data
public class HuResult {

    private String id;
    private Date yangli;
    private String yinli;
    private String wuxing;
    private String chongsha;
    private String baiji;
    private String jishen;
    private String yi;
    private String xiongshen;
    private String ji;

    private Object life;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setYangli(Date yangli) {
        this.yangli = yangli;
    }

    public Date getYangli() {
        return yangli;
    }

    public void setYinli(String yinli) {
        this.yinli = yinli;
    }

    public String getYinli() {
        return yinli;
    }

    public void setWuxing(String wuxing) {
        this.wuxing = wuxing;
    }

    public String getWuxing() {
        return wuxing;
    }

    public void setChongsha(String chongsha) {
        this.chongsha = chongsha;
    }

    public String getChongsha() {
        return chongsha;
    }

    public void setBaiji(String baiji) {
        this.baiji = baiji;
    }

    public String getBaiji() {
        return baiji;
    }

    public void setJishen(String jishen) {
        this.jishen = jishen;
    }

    public String getJishen() {
        return jishen;
    }

    public void setYi(String yi) {
        this.yi = yi;
    }

    public String getYi() {
        return yi;
    }

    public void setXiongshen(String xiongshen) {
        this.xiongshen = xiongshen;
    }

    public String getXiongshen() {
        return xiongshen;
    }

    public void setJi(String ji) {
        this.ji = ji;
    }

    public String getJi() {
        return ji;
    }

}