package com.example.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.config.exception.GlobalException;
import com.example.config.redis.RedisService;
import com.example.config.redis.UserKey;
import com.example.config.util.CodeMsg;
import com.example.config.util.Result;
import com.example.entity.Article;
import com.example.entity.UserAccount;
import com.example.service.impl.ArticleServiceImpl;
import com.example.service.impl.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static Logger log = LoggerFactory.getLogger(IndexControl.class);

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
        UserAccount user = userService.selectOne(new EntityWrapper<UserAccount>()
                .eq("user_phone",userAccount.getUserPhone()));
        if(article.getArticleId() == null){
            if(article.getArticleTagId() == null){
                article.setArticleTagId(1);
            }
            article.setStatus("1000");
            article.setCreateTime(new Date());
            if(StringUtils.isEmpty(article.getArticleTitle())){
                article.setArticleTitle(user.getUserName());
            }
            article.setUserId(user.getUserUuid());
            articleService.insert(article);
        }
        else {
            if(user.getUserUuid() != article.getUserId()){
                throw new GlobalException(CodeMsg.NO_PERMISSION_EDITOR);
            }
            articleService.insertOrUpdate(article);
        }

        return  Result.success(null);
    }


    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result List(HttpServletRequest request, HttpServletResponse response, Article article,UserAccount userAccount) {
        List articleList = articleService.selectArticleList(article,userAccount);
        return  Result.success(articleList);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public Result detail(HttpServletRequest request, HttpServletResponse response, Article article,UserAccount userAccount) {
        List articleList = articleService.selectArticleList(article,userAccount);
        return  Result.success(articleList.get(0));
    }

    @RequestMapping(value = "/classify", method = RequestMethod.POST)
    public Result classify(HttpServletRequest request, HttpServletResponse response) {
        String param = request.getParameter("exculde");
        List classify = articleService.classify(param);
        return  Result.success(classify);
    }

    @RequestMapping(value = "/isEdit", method = RequestMethod.POST)
    public Result isEdit(HttpServletRequest request, HttpServletResponse response) {
        String param = request.getParameter("userPhone");
        Boolean flag = articleService.isEdit(request,param);
        return  Result.success(flag);
    }
}
