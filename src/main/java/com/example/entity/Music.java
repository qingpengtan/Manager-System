package com.example.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

public class Music {

    @TableId(value="music_id",type= IdType.AUTO)
    private Integer musicId;

    @TableField("user_phone")
    private String userPhone;

    @TableField("article_id")
    private Integer articleId;

    @TableField("music_name")
    private String musicName;

    @TableField("music_author")
    private String musicAuthor;

    @TableField("music_pic")
    private String musicPic;

    @TableField("music_url")
    private String musicUrl;

    private String status;

    @TableField("create_time")
    private Date createTime;

    @TableField("music_lrc")
    private String musicLrc;

    public Integer getMusicId() {
        return musicId;
    }

    public void setMusicId(Integer musicId) {
        this.musicId = musicId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName == null ? null : musicName.trim();
    }

    public String getMusicAuthor() {
        return musicAuthor;
    }

    public void setMusicAuthor(String musicAuthor) {
        this.musicAuthor = musicAuthor == null ? null : musicAuthor.trim();
    }

    public String getMusicPic() {
        return musicPic;
    }

    public void setMusicPic(String musicPic) {
        this.musicPic = musicPic == null ? null : musicPic.trim();
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl == null ? null : musicUrl.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMusicLrc() {
        return musicLrc;
    }

    public void setMusicLrc(String musicLrc) {
        this.musicLrc = musicLrc == null ? null : musicLrc.trim();
    }
}