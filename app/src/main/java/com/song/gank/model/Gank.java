package com.song.gank.model;

/**
 * Created by song on 2017/3/4.
 * Email：815464927@qq.com
 */

public class Gank {
    private String time;
    private String decs;
    private String url;
    private String who;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDecs() {
        return decs;
    }

    public void setDecs(String decs) {
        this.decs = decs;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public Gank(String decs, String url, String who, String time) {
        this.decs = decs;
        this.url = url;
        this.who = who;
        this.time = time;
    }
}
