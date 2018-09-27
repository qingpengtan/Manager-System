package com.example.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ArticleDao extends BaseMapper<Article> {

    @Select("select a.article_id as articleId,a.article_tag_id as articleTag,u.user_name as userName, u.user_phone as userPhone," +
            " a.article_title as articleTitle, a.content, a.status, DATE_FORMAT(a.create_time ,'%Y-%m-%d %H:%i') as createTime " +
            " from article a left join user_account u on a.user_id = u.user_uuid")
    List<Map<String,Object>> selectArticleList(Article article);
}
