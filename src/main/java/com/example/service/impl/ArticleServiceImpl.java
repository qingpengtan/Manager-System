package com.example.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.dao.ArticleDao;
import com.example.entity.Article;
import com.example.service.ArticleService;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl  extends ServiceImpl<ArticleDao, Article> implements ArticleService {
}
