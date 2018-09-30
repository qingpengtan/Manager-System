package com.example.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.entity.Article;
import com.example.entity.UserAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ArticleDao extends BaseMapper<Article> {

	List<Map<String,Object>> selectArticleList(@Param("article") Article article);



	List<Map<String,Object>> classify(@Param("exclude")String param);



}
