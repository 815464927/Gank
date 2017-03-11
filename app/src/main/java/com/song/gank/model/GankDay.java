package com.song.gank.model;

/**
 * Created by song on 2017/2/6.
 * Email：815464927@qq.com
 */

public class GankDay {
    private String desc;
    private String url;
    private String who;
    private String[] images;
    private String type;
    private boolean isShowType;

    public boolean isShowType() {
        return isShowType;
    }

    public void setShowType(boolean showType) {
        isShowType = showType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public GankDay(String desc, String url, String who, String type, boolean isShowType) {
        this.desc = desc;
        this.url = url;
        this.who = who.length()>0?who:"None";
        this.type = type;
        this.isShowType = isShowType;
    }

    public GankDay(String desc, String url, String who, String[] images,
                   String type, boolean isShowType) {
        this.desc = desc;
        this.url = url;
        this.who = who.length()>0?who:"None";
        this.images = images;
        this.type = type;
        this.isShowType = isShowType;
    }
}
