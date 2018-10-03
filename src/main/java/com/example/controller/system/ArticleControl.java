package com.example.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.config.redis.RedisService;
import com.example.config.redis.UserKey;
import com.example.config.util.Result;
import com.example.entity.Article;
import com.example.entity.UserAccount;
import com.example.service.ArticleService;
import com.example.service.impl.ArticleServiceImpl;
import com.example.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sys/article")
public class ArticleControl {

    @Autowired
    ArticleServiceImpl articleService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    RedisService redisService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result List(HttpServletRequest request, HttpServletResponse response, Article article,UserAccount userAccount) {
        List articleList = articleService.selectArticleList(article,userAccount);
        return  Result.success(articleList);
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(HttpServletRequest request, HttpServletResponse response, Article article) {
        String token = request.getHeader("token");
        UserAccount userAccount = redisService.get(UserKey.token, token, UserAccount.class);
        if(article.getArticleId() == null){
            article.setCreateTime(new Date());
            UserAccount user = userService.selectOne(new EntityWrapper<UserAccount>()
                    .eq("user_phone",userAccount.getUserPhone()));
            article.setUserId(user.getUserUuid());
            articleService.insert(article);
        }else{
            articleService.insertOrUpdate(article);
        }
        return  Result.success(null);
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result delete(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        articleService.deleteById(Integer.parseInt(id));
        return  Result.success(null);
    }
}
