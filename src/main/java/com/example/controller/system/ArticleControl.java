package com.example.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.config.util.Result;
import com.example.entity.Article;
import com.example.service.ArticleService;
import com.example.service.impl.ArticleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/sys/article")
public class ArticleControl {

    @Autowired
    ArticleServiceImpl articleService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result List(HttpServletRequest request, HttpServletResponse response, Article article) {
        List articleList = articleService.selectArticleList(article);
        return  Result.success(articleList);
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(HttpServletRequest request, HttpServletResponse response, Article article) {
        articleService.insertOrUpdate(article);
        return  Result.success(null);
    }
}
