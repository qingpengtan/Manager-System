package com.example.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.dao.CommentsDao;
import com.example.entity.Comments;
import com.example.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentsServiceImpl  extends ServiceImpl<CommentsDao, Comments> implements CommentsService {


    @Autowired
    CommentsDao commentsDao;


    public List commentsList(Comments comments) {

        return  commentsDao.commentsList(comments.getArticleId());
    }
}
