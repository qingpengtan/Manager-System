package com.example.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

public class Article {
    @TableId(value="article_id",type= IdType.AUTO)
    private Integer articleId;

    @TableField("article_tag_id")
    private Integer articleTagId;

    @TableField("user_id")
    private Integer userId;

    @TableField("article_title")
    private String articleTitle;

    @TableField("article_brief")
    private String articleBrief;

    private String content;

    private String status;

    @TableField("create_time")
    private Date createTime;

    public String getArticleBrief() {
        return articleBrief;
    }

    public void setArticleBrief(String articleBrief) {
        this.articleBrief = articleBrief;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getArticleTagId() {
        return articleTagId;
    }

    public void setArticleTagId(Integer articleTagId) {
        this.articleTagId = articleTagId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle == null ? null : articleTitle.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
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
}