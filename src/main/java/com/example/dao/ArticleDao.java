package com.example.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.example.entity.Article;
import com.example.entity.UserAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface ArticleDao extends BaseMapper<Article> {

	List<Map<String,Object>> selectArticleList(Page page, @Param("article") Article article,
											   @Param("user") UserAccount userAccount,
											   @Param("detail")  String detail);



	List<Map<String,Object>> classify(@Param("exclude")String param);


    HashMap<String,Object> articleDetail( @Param("article") Article article,
						   @Param("user") UserAccount userAccount);
}
