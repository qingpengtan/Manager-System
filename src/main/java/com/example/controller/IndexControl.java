package com.example.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.config.redis.RedisService;
import com.example.config.redis.UserKey;
import com.example.config.util.Result;
import com.example.entity.Article;
import com.example.entity.UserAccount;
import com.example.service.impl.ArticleServiceImpl;
import com.example.service.impl.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/index")
public class IndexControl {


    @Autowired
    ArticleServiceImpl articleService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    RedisService redisService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(HttpServletRequest request, HttpServletResponse response, Article article) {
        String token = request.getHeader("token");
        UserAccount userAccount = redisService.get(UserKey.token, token, UserAccount.class);
            if(StringUtils.isEmpty(article.getArticleTitle())){
                article.setArticleTitle("说说");
            }
            article.setStatus("1000");
            article.setArticleTagId(1);
            article.setCreateTime(new Date());
            UserAccount user = userService.selectOne(new EntityWrapper<UserAccount>()
                    .eq("user_phone",userAccount.getUserPhone()));
            article.setUserId(user.getUserUuid());
            articleService.insert(article);
        return  Result.success(null);
    }


    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result List(HttpServletRequest request, HttpServletResponse response, Article article) {
        List articleList = articleService.selectArticleList(article);
        return  Result.success(articleList);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public Result detail(HttpServletRequest request, HttpServletResponse response, Article article) {
        List articleList = articleService.selectArticleList(article);
        return  Result.success(articleList.get(0));
    }
}
