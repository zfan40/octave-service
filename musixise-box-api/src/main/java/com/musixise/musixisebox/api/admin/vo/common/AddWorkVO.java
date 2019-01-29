package com.musixise.musixisebox.api.admin.vo.common;

import io.swagger.annotations.ApiModelProperty;

public class AddWorkVO {

    @ApiModelProperty(value = "标题", example = "music title")
    private String title;

    @ApiModelProperty(value = "封面", example = "https://gw.alicdn.com/tps/TB1fcMYNVXXXXXqXVXXXXXXXXXX-750-750.png")
    private String cover;

    @ApiModelProperty(value = "描述", example = "")
    private String content;

    @ApiModelProperty(value = "音频地址", example = "http://oiqvdjk3s.bkt.clouddn.com/kuNziglJ_test.txt")
    private String url;

    @ApiModelProperty(value = "作者ID", example = "1111")
    private Long userId;

    @ApiModelProperty(value = "状态 0=正常，1=私有，2=删除", example = "1111")
    private Integer status;

    @ApiModelProperty(value = "收藏数", example = "1111")
    private Integer collectNum;
    @ApiModelProperty(value = "浏览数", example = "1111")
    private Integer pv;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }
}
