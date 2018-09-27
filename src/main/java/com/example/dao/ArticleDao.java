package com.example.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.entity.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleDao extends BaseMapper<Article> {
}
