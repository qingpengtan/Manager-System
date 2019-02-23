package com.example.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.config.exception.GlobalException;
import com.example.config.redis.RedisService;
import com.example.config.redis.UserKey;
import com.example.config.util.CodeMsg;
import com.example.config.util.Result;
import com.example.entity.Comments;
import com.example.entity.UserAccount;
import com.example.service.impl.CommentsServiceImpl;
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
@RequestMapping("/index/comment")
public class CommentControl {

    @Autowired
    UserServiceImpl userService;
    @Autowired
    RedisService redisService;

    @Autowired
    CommentsServiceImpl commentsService;


    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Result comment(HttpServletRequest request, HttpServletResponse response, Comments comments) {
        String token = request.getHeader("token");
        UserAccount userAccount = redisService.get(UserKey.token, token, UserAccount.class);
        if(userAccount == null){
            throw new GlobalException(CodeMsg.OUT_LINE);
        }
        UserAccount user = userService.selectOne(new EntityWrapper<UserAccount>()
                .eq("user_phone",userAccount.getUserPhone()));
        comments.setUserId(user.getUserUuid());
        comments.setStatus("1000");
        comments.setCreateTime(new Date());
        commentsService.insert(comments);
        return  Result.success(null);
    }


    @RequestMapping(value = "/commentList", method = RequestMethod.POST)
    public Result commentList(HttpServletRequest request, HttpServletResponse response, Comments comments) {

        List list = commentsService.commentsList(comments);

        return  Result.success(list);
    }




}
