package com.example.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.config.redis.RedisService;
import com.example.config.redis.UserKey;
import com.example.dao.ArticleDao;
import com.example.entity.Article;
import com.example.entity.UserAccount;
import com.example.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class ArticleServiceImpl  extends ServiceImpl<ArticleDao, Article> implements ArticleService {

    @Autowired
    ArticleDao articleDao;
    @Autowired
    RedisService redisService;
    @Autowired
    UserServiceImpl userService;

    public List selectArticleList(Page page, Article article, UserAccount userAccount, String detial) {
        return articleDao.selectArticleList(page,article,userAccount,detial);
    }

    public List classify(String param) {
        return articleDao.classify(param);
    }

    public Boolean isEdit(HttpServletRequest request, String param) {
        String token = request.getHeader("token");
        UserAccount user = redisService.get(UserKey.token, token, UserAccount.class);
        if(user.getUserPhone().equals(param))
            return true;
        else
            return  false;
    }

}
