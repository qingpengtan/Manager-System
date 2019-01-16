package com.example.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.entity.Article;
import com.example.entity.Comments;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommentsDao extends BaseMapper<Comments> {

    List<Map<String,Object>> commentsList(Integer articleId);
}